package com.mopl.controller.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.mopl.dao.NoticeDAO;
import com.mopl.model.NoticeDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.FileManager;
import com.mopl.util.MyMultipartFile;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class NoticeManageController {

	@RequestMapping(value = "/admin/notice/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		// 넘어온 파라미터 : [페이지번호, size, 검색컬럼, 검색값]
		ModelAndView mav = new ModelAndView("admin/notice/list");
		
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			// 한페이지 표시할 데이터 개수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);

			int dataCount, total_page;
			if (kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);

			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if (kwd.length() != 0) {
				list = dao.listNotice(offset, size, schType, kwd);
			} else {
				list = dao.listNotice(offset, size);
			}

			// 공지글
			List<NoticeDTO> listNotice = null;
			if(current_page == 1) {
				listNotice = dao.listNotice();
			}
			
			/*
			long gap;
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (NoticeDTO dto : list) {
				Date date = sdf.parse(dto.getReg_date());
				// gap = (curDate.getTime() - date.getTime()) / (1000*60*60*24); // 일자
				gap = (curDate.getTime() - date.getTime()) / (1000 * 60 * 60); // 시간
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			*/
			
			// 공지글이 새로 등록된 것과의 시간 차
			long gap;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for (NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), formatter);
				gap = dateTime.until(today, ChronoUnit.HOURS);
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}

			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			listUrl = cp + "/admin/notice/list?" + query;
			articleUrl = cp + "/admin/notice/article?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩 jsp에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("listNotice", listNotice);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JSP로 포워딩
		return mav;
	}

	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("admin/notice/write");
		
		String size = req.getParameter("size");
		
		mav.addObject("mode", "write");
		mav.addObject("size", size);
		
		return mav;
	}

	@RequestMapping(value = "/admin/notice/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		// 넘어온 폼데이터 : 제목, 내용, 공지여부 [, 파일]
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String size = req.getParameter("size");
		try {
			NoticeDTO dto = new NoticeDTO();
			
			System.out.println(info);
			System.out.println(req.getParameter("subject"));
			
			dto.setMemberIdx(info.getMemberIdx());
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);

			dto.setListFile(listFile);

			dao.insertNotice(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?size=" + size);
	}

	@RequestMapping(value = "/admin/notice/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			// 조회수
			dao.updateHitCount(num);

			// 게시물 가져오기
			NoticeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?" + query);
			}

			// 스마트 에디터를 사용하므로 주석 처리
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 이전글/다음글
			NoticeDTO prevDto = dao.findByPrev(dto.getNum(), schType, kwd);
			NoticeDTO nextDto = dao.findByNext(dto.getNum(), schType, kwd);

			// 파일
			List<NoticeDTO> listFile = dao.listNoticeFile(num);

			ModelAndView mav = new ModelAndView("admin/notice/article");
			
			mav.addObject("dto", dto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		// 넘어온 파라미터 : 글번호, 페이지번호, size
		NoticeDAO dao = new NoticeDAO();

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long num = Long.parseLong(req.getParameter("num"));

			NoticeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
			}

			// 파일
			List<NoticeDTO> listFile = dao.listNoticeFile(num);

			ModelAndView mav = new ModelAndView("admin/notice/write");
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);

			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		// 폼 데이터 : 글번호, 제목, 내용, 공지여부, [새로운파일, ] 페이지번호, size
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info.getRole()!=0) {
			return new ModelAndView("redirect:/");
		}
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			NoticeDTO dto = new NoticeDTO();
			
			dto.setNum(Long.parseLong(req.getParameter("num")));
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			List<MyMultipartFile> listFile = fileManager.doFileUpload(req.getParts(), pathname);

			dto.setListFile(listFile);

			dao.updateNotice(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		// 넘어온 파라미터 : 글번호, 파일번호, 페이지번호, size
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			long fileNum = Long.parseLong(req.getParameter("fileNum"));
			NoticeDTO dto = dao.findByFileId(fileNum);
			if (dto != null) {
				// 파일삭제
				fileManager.doFiledelete(pathname, dto.getSaveFilename());
				
				// 테이블 파일 정보 삭제
				dao.deleteNoticeFile("one", fileNum);
			}

			// 다시 수정 화면으로
			return new ModelAndView("redirect:/admin/notice/update?num=" + num + "&page=" + page + "&size=" + size);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}

	@RequestMapping(value = "/admin/notice/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			NoticeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?" + query);
			}

			// 파일삭제
			List<NoticeDTO> listFile = dao.listNoticeFile(num);
			for (NoticeDTO vo : listFile) {
				fileManager.doFiledelete(pathname, vo.getSaveFilename());
			}
			dao.deleteNoticeFile("all", num);

			// 게시글 삭제
			dao.deleteNotice(num);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

	@RequestMapping(value = "/admin/notice/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;

		try {
			long fileNum = Long.parseLong(req.getParameter("fileNum"));

			NoticeDTO dto = dao.findByFileId(fileNum);
			if (dto != null) {
				b = fileManager.doFiledownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ( ! b ) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일다운로드가 실패 했습니다.');history.back();</script>");
		}
	}

	@RequestMapping(value = "/admin/notice/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글다중삭제
		// 넘어온 파라미터 : 글번호들, 페이지번호, size [, 검색컬럼, 검색값]
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "size=" + size + "&page=" + page;

		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			String[] nn = req.getParameterValues("nums");
			long nums[] = null;
			nums = new long[nn.length];
			for (int i = 0; i < nn.length; i++) {
				nums[i] = Long.parseLong(nn[i]);
			}

			NoticeDAO dao = new NoticeDAO();

			// 파일 삭제
			for (int i = 0; i < nums.length; i++) {
				List<NoticeDTO> listFile = dao.listNoticeFile(nums[i]);
				for (NoticeDTO vo : listFile) {
					fileManager.doFiledelete(pathname, vo.getSaveFilename());
				}
				dao.deleteNoticeFile("all", nums[i]);
			}

			// 게시글 삭제
			dao.deleteNotice(nums);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}

}

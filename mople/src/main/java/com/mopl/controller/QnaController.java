package com.mopl.controller;

import java.io.IOException;
import java.util.List;

import com.mopl.dao.QnaDAO;
import com.mopl.model.QnaDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class QnaController {

	@RequestMapping(value = "/qna/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		ModelAndView mav = new ModelAndView("qna/list");
		
		QnaDAO dao = new QnaDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}

			kwd = util.decodeUrl(kwd);

			// 전체 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(kwd);
			}
			
			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<QnaDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listContent(offset, size);
			} else {
				list = dao.listContent(offset, size, kwd);
			}

			String query = "";
			if (kwd.length() != 0) {
				query = "kwd=" + util.encodeUrl(kwd);
			}

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/qna/list";
			String articleUrl = cp + "/qna/article?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JSP로 포워딩
		return mav;
	}

	@RequestMapping(value = "/qna/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("qna/write");		
		mav.addObject("mode", "write");
		return mav;
	}

	@RequestMapping(value = "/qna/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		QnaDAO dao = new QnaDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			QnaDTO dto = new QnaDTO();

			// userId는 세션에 저장된 정보
			dto.setMemberIdx(info.getMemberIdx());
			
			// 파라미터
			dto.setSecret(Integer.parseInt(req.getParameter("secret")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dao.insertContent(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list");
	}

	@RequestMapping(value = "/qna/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		QnaDAO dao = new QnaDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (kwd.length() != 0) {
				query += "&kwd=" + util.encodeUrl(kwd);
			}

			// 게시물 가져오기
			QnaDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/qna/list?" + query);
			}
			
			if(dto.getSecret() == 1) {
				if( dto.getMemberIdx()!=info.getMemberIdx() && info.getRole() != 0 ) {
					return new ModelAndView("redirect:/qna/list?" + query);
				}
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			if(dto.getAnswer() != null) {
				dto.setAnswer(dto.getAnswer().replaceAll("\n", "<br>"));
			}

			// 이전글 다음글
			QnaDTO prevDto = dao.findByPrev(dto.getNum(), kwd);
			QnaDTO nextDto = dao.findByNext(dto.getNum(), kwd);

			ModelAndView mav = new ModelAndView("qna/article");
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?" + query);
	}

	@RequestMapping(value = "/qna/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		QnaDAO dao = new QnaDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			QnaDTO dto = dao.findById(num);

			if (dto == null) {
				return new ModelAndView("redirect:/qna/list?page=" + page);
			}

			// 게시물을 올린 사용자가 아니면
			if ( dto.getMemberIdx() != info.getMemberIdx()) {
				return new ModelAndView("redirect:/qna/list?page=" + page);
			}

			ModelAndView mav = new ModelAndView("qna/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?page=" + page);
	}

	@RequestMapping(value = "/qna/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		QnaDAO dao = new QnaDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		try {
			QnaDTO dto = new QnaDTO();
			
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSecret(Integer.parseInt(req.getParameter("secret")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dto.setMemberIdx(info.getMemberIdx());

			dao.updateContent(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?page=" + page);
	}

	@RequestMapping(value = "/qna/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		QnaDAO dao = new QnaDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String mode = req.getParameter("mode");
			
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (kwd.length() != 0) {
				query += "&kwd=" + util.encodeUrl(kwd);
			}
			
			if(mode.equals("answer") && info.getRole() == 0) {
				// 답변 삭제
				QnaDTO dto = new QnaDTO();
				dto.setNum(num);
				dto.setAnswer("");
				dto.setAnswerIdx(0);
				dao.updateAnswer(dto);
			} else if(mode.equals("question")) {
				// 질문 삭제
				dao.deleteContent(num, info.getMemberIdx(), info.getRole());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/qna/list?" + query);
	}
}

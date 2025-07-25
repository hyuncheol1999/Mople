package com.mopl.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.MeetingBoardDAO;
import com.mopl.model.MeetingBoardDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.annotation.ResponseBody;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.FileManager;
import com.mopl.util.MyMultipartFile;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class MeetingBoardController {

	// 글 목록 조회
	@RequestMapping(value = "/meetingBoard/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/meetingBoard/list");

		MeetingBoardDAO dao = new MeetingBoardDAO();
		MyUtil util = new MyUtil();

		try {
			String meetingIdxparam = req.getParameter("meetingIdx");
			if (meetingIdxparam == null || meetingIdxparam.isEmpty()) {
				return new ModelAndView("redirect:/list");
			}

			long meetingIdx = Long.parseLong(meetingIdxparam);

			int current_page = 1;
			String page = req.getParameter("page");
			if (page != null && !page.isEmpty()) {
				current_page = Integer.parseInt(page);
			}

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");

			if (kwd == null || kwd.trim().isEmpty()) {
				kwd = "";
				schType = "all";
			} else {
				kwd = util.decodeUrl(kwd);
				if (schType == null || schType.trim().isEmpty()) {
					schType = "all";
				}
			}

			String filter = req.getParameter("filter");

			int size = 7;
			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			// 데이터 수, 리스트 조회
			int dataCount = dao.dataCount(meetingIdx, filter, schType, kwd);
			List<MeetingBoardDTO> list = dao.searchBoard(meetingIdx, offset, size, filter, schType, kwd);

			// 페이징 계산
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			StringBuilder query = new StringBuilder("meetingIdx=" + meetingIdx);
			if (filter != null && !filter.isEmpty())
				query.append("&filter=").append(filter);
			if (!kwd.trim().isEmpty())
				query.append("&schType=").append(schType).append("&kwd=").append(util.encodeUrl(kwd));

			String cp = req.getContextPath();
			String listUrl = cp + "/meetingBoard/list?" + query;
			String articleUrl = cp + "/meetingBoard/view?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("meetingIdx", meetingIdx);
			mav.addObject("filter", filter);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("page", current_page);
			mav.addObject("paging", paging);
			mav.addObject("dataCount", dataCount);
			mav.addObject("articleUrl", articleUrl);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 글 작성 폼
	@RequestMapping(value = "/meetingBoard/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meetingBoard/write");

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
		mav.addObject("meetingIdx", meetingIdx);
		mav.addObject("mode", "write");

		String imagePath = session.getServletContext().getRealPath("/uploads/photo");

		File imageDir = new File(imagePath);
		String[] imageFiles = imageDir.list((dir, name) -> name.matches(".*\\.(png|jpg|jpeg|gif)$"));

		mav.addObject("imageList", imageFiles);

		return mav;
	}

	// 글 저장
	@RequestMapping(value = "/meetingBoard/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo";

		long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));

		try {
			MeetingBoardDTO dto = new MeetingBoardDTO();

			dto.setUserNickName(info.getUserNickName());
			dto.setMemberIdx(info.getMemberIdx());
			dto.setMeetingIdx(meetingIdx);
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setFilter(req.getParameter("filter"));

			long num = dao.insertMeetingBoard(dto);

			dto.setNum(num);

			List<String> imageFileNames = new ArrayList<>();

			for (Part part : req.getParts()) {
				if (part.getName().equals("uploadFiles") && part.getSubmittedFileName() != null && part.getSize() > 0) {

					MyMultipartFile mf = fileManager.doFileUpload(part, pathname);
					if (mf != null) {
						imageFileNames.add(mf.getSaveFilename());
					}
				}
			}

			if (!imageFileNames.isEmpty()) {
				dto.setImageFileNames(imageFileNames);
				dao.insertMeetingBoardImg(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meetingBoard/list?meetingIdx=" + meetingIdx);
	}

	// 글 보기
	@RequestMapping(value = "/meetingBoard/view", method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		MeetingBoardDAO dao = new MeetingBoardDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		String meetingIdxParam = req.getParameter("meetingIdx");
		String numParam = req.getParameter("num");

		if (meetingIdxParam == null || numParam == null) {
			return new ModelAndView("redirect:/meetingBoard/list");
		}

		try {
			long meetingIdx = Long.parseLong(meetingIdxParam);
			long num = Long.parseLong(numParam);

			String query = "meetingIdx=" + meetingIdx + "&page=" + (page != null ? page : "1");

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");

			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (!kwd.isEmpty()) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			MeetingBoardDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/meetingBoard/list?" + query);
			}

			List<String> imageList = dao.listMeetingBoardImage(num);
			dto.setImageFileNames(imageList);

			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전 글, 다음 글
			MeetingBoardDTO prevDto = dao.findByPrev(dto.getNum(), schType, kwd);
			MeetingBoardDTO nextDto = dao.findByNext(dto.getNum(), schType, kwd);

			// 좋아요 여부 확인
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			boolean liked = false;
			int likeCount = dao.countMeetingBoardLike(num);

			if (info != null) {
				liked = dao.isUserBoardLike(num, info.getMemberIdx());
			}

			ModelAndView mav = new ModelAndView("meetingBoard/view");
			mav.addObject("dto", dto);
			mav.addObject("imageList", imageList);
			mav.addObject("meetingIdx", meetingIdx);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("liked", liked);
			mav.addObject("likeCount", likeCount);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			return mav;

		} catch (NumberFormatException e) {
			return new ModelAndView("redirect:/meetingBoard/list");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meetingBoard/list");
	}

	// 글 수정 폼
	@RequestMapping(value = "/meetingBoard/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		String page = req.getParameter("page");
		long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));

		try {
			long num = Long.parseLong(req.getParameter("num"));
			MeetingBoardDTO dto = dao.findById(num);

			if (dto == null || dto.getMemberIdx() != info.getMemberIdx()) {
				return new ModelAndView("redirect:/meetingBoard/list?page=" + page);
			}

			ModelAndView mav = new ModelAndView("meetingBoard/write");

			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");
			mav.addObject("meetingIdx", meetingIdx);

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meetingBoard/list?meetingIdx=" + meetingIdx + "&page=" + page);
	}

	// 수정 처리
	@RequestMapping(value = "/meetingBoard/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		String page = req.getParameter("page");
		long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));

		try {
			MeetingBoardDTO dto = new MeetingBoardDTO();

			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setMemberIdx(info.getMemberIdx());

			dao.updateMeetingBoard(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/meetingBoard/list?meetingIdx=" + meetingIdx + "&page=" + page);
	}

	// 글 삭제
	@RequestMapping(value = "/meetingBoard/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();
		MyUtil util = new MyUtil();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		String page = req.getParameter("page");
		String meetingIdx = req.getParameter("meetingIdx");
		String filter = req.getParameter("filter");
		String schType = req.getParameter("schType");
		String kwd = req.getParameter("kwd");

		StringBuilder query = new StringBuilder("meetingIdx=" + meetingIdx);
		if (page != null)
			query.append("&page=").append(page);
		if (filter != null)
			query.append("&filter=").append(filter);
		if (kwd != null) {
			kwd = util.decodeUrl(kwd);
			query.append("&schType=").append(schType != null ? schType : "all").append("&kwd=")
					.append(util.encodeUrl(kwd));
		}

		try {
			long num = Long.parseLong(req.getParameter("num"));
			dao.deleteMeetingBoard(num, info.getMemberIdx());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meetingBoard/list?" + query);
	}

	// 좋아요 AJAX 처리
	@ResponseBody
	@RequestMapping(value = "/meetingBoardLike", method = RequestMethod.POST)
	public Map<String, Object> insertMeetingBoardLike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Map<String, Object> model = new HashMap<String, Object>();
		MeetingBoardDAO dao = new MeetingBoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "false";
		int meetingBoardLikeCount = 0;
		boolean liked = false;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String userLiked = req.getParameter("userLiked");

			if (userLiked.equals("true")) {
				dao.deleteMeetingBoardLike(num, info.getMemberIdx());
				liked = false;
			} else {
				dao.insertMeetingBoardLike(num, info.getMemberIdx());
				liked = true;
			}

			meetingBoardLikeCount = dao.countMeetingBoardLike(num);
			state = "success";

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);
		model.put("liked", liked);
		model.put("meetingBoardLikeCount", meetingBoardLikeCount);

		return model;
	}

	// 댓글 목록
	@RequestMapping(value = "/meetingBoard/listReply", method = RequestMethod.GET)
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();
		MyUtil util = new MyUtil();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 응답
			return null;
		}

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");

			int current_page = 1;
			if (pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}

			int size = 10;
			int total_page = 0;
			int replyCount = 0;

			replyCount = dao.dataCountReply(num);
			total_page = util.pageCount(replyCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<MeetingBoardDTO> listReply = dao.listReply(num, offset, size, info.getMemberIdx());

			for (MeetingBoardDTO dto : listReply) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}

			String paging = util.pagingMethod(current_page, total_page, "listPage");

			ModelAndView mav = new ModelAndView("meetingBoard/listReply");

			mav.addObject("listReply", listReply);
			mav.addObject("pageNo", current_page);
			mav.addObject("replyCount", replyCount);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
			throw e;
		}
	}

	// 댓글, 대댓글 저장 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/meetingBoard/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		MeetingBoardDAO dao = new MeetingBoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			model.put("state", "loginFail");
			return model;
		}

		String state = "false";

		try {
			MeetingBoardDTO dto = new MeetingBoardDTO();

			long num = Long.parseLong(req.getParameter("num"));
			dto.setNum(num);
			dto.setMemberIdx(info.getMemberIdx());
			dto.setContent(req.getParameter("content"));
			String parentNum = req.getParameter("parentNum");
			if (parentNum != null) {
				dto.setParentNum(Long.parseLong(parentNum));
			}

			dao.insertReply(dto);

			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("state", state);

		return model;
	}

	// 댓글 또는 대댓글 삭제 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/meetingBoard/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		MeetingBoardDAO dao = new MeetingBoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "false";
		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));
			dao.deleteReply(replyNum, info.getMemberIdx());

			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}

	// 대댓글 목록 - AJAX:TEXT
	@RequestMapping(value = "/meetingBoard/listReplyAnswer", method = RequestMethod.GET)
	public ModelAndView listReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingBoardDAO dao = new MeetingBoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			long parentNum = Long.parseLong(req.getParameter("parentNum"));

			List<MeetingBoardDTO> listReplyAnswer = dao.listReplyAnswer(parentNum, info.getMemberIdx());

			for (MeetingBoardDTO dto : listReplyAnswer) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}

			ModelAndView mav = new ModelAndView("meetingBoard/listReplyAnswer");

			mav.addObject("listReplyAnswer", listReplyAnswer);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
			throw e;
		}
	}

	// 대댓글 개수 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/meetingBoard/countReplyAnswer", method = RequestMethod.POST)
	public Map<String, Object> countReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		MeetingBoardDAO dao = new MeetingBoardDAO();

		int count = 0;

		try {
			long parentNum = Long.parseLong(req.getParameter("parentNum"));
			count = dao.dataCountReplyAnswer(parentNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("count", count);

		return model;
	}

}

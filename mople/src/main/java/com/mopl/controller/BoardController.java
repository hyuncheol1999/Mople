package com.mopl.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.BoardDAO;
import com.mopl.model.BoardDTO;
import com.mopl.model.ReplyDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.annotation.ResponseBody;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
	@RequestMapping(value = "/bbs/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		ModelAndView mav = new ModelAndView("bbs/list");
		
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
					
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<BoardDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listBoard(offset, size);
			} else {
				list = dao.listBoard(offset, size, schType, kwd);
			}
			
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			// 페이징
			String cp = req.getContextPath();
			String listUrl = cp + "/bbs/list";
			String articleUrl = cp + "/bbs/article?page=" + current_page;
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/bbs/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("bbs/write");
		mav.addObject("mode", "write");
		return mav;
	}

	@RequestMapping(value = "/bbs/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			BoardDTO dto = new BoardDTO();

			// userId는 세션에 저장된 정보
			dto.setMemberIdx(info.getMemberIdx());

			// 파라미터
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list");
	}

	@RequestMapping(value = "/bbs/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

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

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			BoardDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/bbs/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			BoardDTO prevDto = dao.findByPrev(dto.getNum(), schType, kwd);
			BoardDTO nextDto = dao.findByNext(dto.getNum(), schType, kwd);

			// 로그인 유저의 게시글 공감 여부
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			boolean isUserLiked = false;
			if (info != null) {
			    isUserLiked = dao.isUserBoardLike(num, info.getMemberIdx());
			}

			
			
			ModelAndView mav = new ModelAndView("bbs/article");
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			mav.addObject("isUserLiked", isUserLiked);
			
			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?" + query);
	}

	@RequestMapping(value = "/bbs/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		try {
			long num = Long.parseLong(req.getParameter("num"));
			BoardDTO dto = dao.findById(num);
			
			if(dto == null) {
				return new ModelAndView("redirect:/bbs/list?page=" + page);
			}
			
			// 게시물 작성자가 아니면
			if( dto.getMemberIdx()!=info.getMemberIdx()) {
				return new ModelAndView("redirect:/bbs/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("bbs/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?page=" + page);
	}

	@RequestMapping(value = "/bbs/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			BoardDTO dto = new BoardDTO();
			
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setMemberIdx(info.getMemberIdx());
			
			dao.updateBoard(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?page=" + page);
	}

	@RequestMapping(value = "/bbs/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
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
			
			dao.deleteBoard(num, info.getMemberIdx(), info.getRole());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?" + query);
	}
	
	// 게시글 공감 저장 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/insertBoardLike", method = RequestMethod.POST)
	public Map<String, Object> insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터 : 글번호, 공감/공감취소여부
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		int boardLikeCount = 0;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String userLiked = req.getParameter("userLiked");
			
			if(userLiked.equals("true")) {
				// 공감 취소
				dao.deleteBoardLike(num, info.getMemberIdx());
				
			} else {
				// 공감
				dao.insertBoardLike(num, info.getMemberIdx());
			}
			
			boardLikeCount = dao.countBoardLike(num);
			
			state = "true";
			
		} catch (SQLException e) {
			state = "liked";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		model.put("boardLikeCount", boardLikeCount);

		return model;
	}

	// 리플 리스트 - AJAX:TEXT
	@RequestMapping(value = "/bbs/listReply", method = RequestMethod.GET)	
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if(pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			
			int size = 5;
			int total_page = 0;
			int replyCount = 0;
			if (info != null) {
				replyCount = dao.dataCountReply(num, info.getMemberIdx(), info.getRole());
			}
			total_page = util.pageCount(replyCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<ReplyDTO> listReply;
			if (info != null) {
			    listReply = dao.listReply(num, offset, size, info.getMemberIdx(), info.getRole());
			} else {
			    listReply = dao.listReply(num, offset, size); 
			}
			
			// 엔터를 <br>로
			for(ReplyDTO dto : listReply) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				
				// 유저의 좋아요/싫어요 유무
				if (info != null) {
					dto.setUserReplyLike(dao.userReplyLike(dto.getReplyNum(), info.getMemberIdx()));
				}
			}
			
			// 페이징 처리 : AJAX 용
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			ModelAndView mav = new ModelAndView("bbs/listReply");
			
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

	// 리플 또는 답글 저장 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		
		try {
			ReplyDTO dto = new ReplyDTO();
			
			long num = Long.parseLong(req.getParameter("num"));
			dto.setNum(num);
			dto.setMemberIdx(info.getMemberIdx());
			dto.setContent(req.getParameter("content"));
			String parentNum = req.getParameter("parentNum");
			if(parentNum != null) {
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

	// 리플 또는 답글 삭제 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));
			dao.deleteReply(replyNum, info.getMemberIdx(), info.getRole());
			
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);
		
		return model;
	}

	// 댓글 좋아요 / 싫어요 저장 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/insertReplyLike", method = RequestMethod.POST)
	public Map<String, Object> insertReplyLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		int likeCount = 0;
		int disLikeCount = 0;
		
		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));
			int replyLike = Integer.parseInt(req.getParameter("replyLike"));
			
			ReplyDTO dto = new ReplyDTO();
			
			dto.setReplyNum(replyNum);
			dto.setReplyLike(replyLike);
			dto.setMemberIdx(info.getMemberIdx());
			
			dao.insertReplyLike(dto);
			Map<String, Integer> map = dao.countReplyLike(replyNum);
			
			if(map.containsKey("likeCount")) {
				likeCount = map.get("likeCount");
			}
			if(map.containsKey("disLikeCount")) {
				disLikeCount = map.get("disLikeCount");
			}
			
			state = "true";
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				state = "liked";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);
		model.put("likeCount", likeCount);
		model.put("disLikeCount", disLikeCount);
		
		return model;
	}

	// 리플의 답글 리스트 - AJAX:TEXT
	@RequestMapping(value = "/bbs/listReplyAnswer", method = RequestMethod.GET)
	public ModelAndView listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			long parentNum = Long.parseLong(req.getParameter("parentNum"));
			
			List<ReplyDTO> listReplyAnswer;

	        if (info != null) {
	            listReplyAnswer = dao.listReplyAnswer(parentNum, info.getMemberIdx(), info.getRole());
	        } else {
	            listReplyAnswer = dao.listReplyAnswer(parentNum); // 비회원용 오버로딩 메서드
	        }
			
			for(ReplyDTO dto : listReplyAnswer) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}
			
			ModelAndView mav = new ModelAndView("bbs/listReplyAnswer");
			mav.addObject("listReplyAnswer", listReplyAnswer);
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
			throw e;
		}
		
	}

	// 리플의 답글 개수 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/countReplyAnswer", method = RequestMethod.POST)
	public Map<String, Object> countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		int count = 0;
		
		try {
			long parentNum = Long.parseLong(req.getParameter("parentNum"));
			count = dao.dataCountReplyAnswer(parentNum, info.getMemberIdx(), info.getRole());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("count", count);

		return model;
	}

	// 댓글 숨김/표시 : AJAX-JSON
	@ResponseBody
	@RequestMapping(value = "/bbs/replyShowHide", method = RequestMethod.POST)
	public Map<String, Object> replyShowHide(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "true";
		
		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));
			int showReply = Integer.parseInt(req.getParameter("showReply"));
			
			dao.updateReplyShowHide(replyNum, showReply, info.getMemberIdx());
			
		} catch (Exception e) {
			state = "false";
			e.printStackTrace();
		}
		
		model.put("state", state);

		return model;
	}
}

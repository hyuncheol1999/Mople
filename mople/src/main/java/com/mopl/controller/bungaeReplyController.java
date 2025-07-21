package com.mopl.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.BungaeReplyDAO;
import com.mopl.model.BungaeReplyDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.annotation.ResponseBody;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class bungaeReplyController {
	// 리플 리스트 - AJAX:TEXT
		@RequestMapping(value = "/bungaeMeeting/replyList", method = RequestMethod.GET)	
		public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			BungaeReplyDAO dao = new BungaeReplyDAO();
			MyUtil util = new MyUtil();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			try {
				long replyNum = Long.parseLong(req.getParameter("replyNum"));
				String pageNo = req.getParameter("pageNo");
				int current_page = 1;
				if(pageNo != null) {
					current_page = Integer.parseInt(pageNo);
				}
				
				int size = 5;
				int total_page = 0;
				int replyCount = 0;
				
				replyCount = dao.dataCountReply(replyNum, info.getMemberIdx(), 
						info.getRole());
				total_page = util.pageCount(replyCount, size);
				if(current_page > total_page) {
					current_page = total_page;
				}
				
				int offset = (current_page - 1) * size;
				if(offset < 0) offset = 0;
				
				List<BungaeReplyDTO> listReply = dao.listReply(replyNum, offset, size, 
						info.getMemberIdx(), info.getRole());
				
				// 엔터를 <br>로
				for(BungaeReplyDTO dto : listReply) {
					dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				}
				
				// 페이징 처리 : AJAX 용
				String paging = util.pagingMethod(current_page, total_page, "listPage");
				
				ModelAndView mav = new ModelAndView("/bungaeMeeting/bungaeMeetingDetail");
				
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
		@RequestMapping(value = "/bungaeMeeting/insertReply", method = RequestMethod.POST)
		public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			Map<String, Object> model = new HashMap<String, Object>();
			
			BungaeReplyDAO dao = new BungaeReplyDAO();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String state = "false";
			
			try {
				BungaeReplyDTO dto = new BungaeReplyDTO();
				
				long replyNum = Long.parseLong(req.getParameter("replyNum"));
				dto.setReplyNum(replyNum);
				dto.setMemberIdx(info.getMemberIdx());
				dto.setContent(req.getParameter("content"));
								
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
			
			BungaeReplyDAO dao = new BungaeReplyDAO();
			
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

}

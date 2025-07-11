package com.mopl.controller;

import java.io.IOException;

import com.mopl.dao.MeetingDAO;
import com.mopl.model.MeetingDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MeetingController {
	// 모임 리스트
	@RequestMapping(value = "/meeting/meetingList", method = RequestMethod.GET)
	public ModelAndView meetingList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingList");
		
		return mav;
	}	

	// 모임 상세
	// 1. 모임 참여 버튼 - 모임 인원 / 관리자는 볼 수 없게 수정
	@RequestMapping(value = "/meeting/meetingDetail", method = RequestMethod.GET)
	public ModelAndView meetingDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingDetail");
		MeetingDAO dao = new MeetingDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return mav;
	}	

	// 모임상세 - 홈 화면
	@RequestMapping(value = "/meeting/meetingHome", method = RequestMethod.GET)
	public ModelAndView meetingHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingHome");
		
		return mav;
	}	

	// 모임상세 - 정모 일정
	@RequestMapping(value = "/meeting/meetingSchedule", method = RequestMethod.GET)
	public ModelAndView meetingSchedule(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingSchedule");
		
		return mav;
	}	

	// 모임상세 - 사진첩
	@RequestMapping(value = "/meeting/meetingAlbum", method = RequestMethod.GET)
	public ModelAndView meetingAlbum(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingAlbum");
		
		return mav;
	}	
	
	// 모임 생성 폼
	@RequestMapping(value = "/meeting/meetingCreate", method = RequestMethod.GET)
	public ModelAndView meetingCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingCreate");
		mav.addObject("mode", "meetingCreate");
		return mav;
	}	
	
	// 모임 생성
	@RequestMapping(value = "/meeting/meetingCreate", method = RequestMethod.POST)
	public ModelAndView meetingCreateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MeetingDAO dao = new MeetingDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			MeetingDTO dto = new MeetingDTO();
			dto.setMeetingName(req.getParameter("meetingName"));
			dto.setMeetingDesc(req.getParameter("meetingDesc"));
			dto.setMeetingProfilePhoto(req.getParameter("meetingProfilePhoto"));
			dto.setRegionIdx(Integer.parseInt(req.getParameter("regionIdx")));
			dto.setSportIdx(Integer.parseInt(req.getParameter("sportIdx")));
			
			dto.setMemberIdx(info.getMemberIdx());
			dto.setRole(0);
			
			dao.insertMeeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/meeting/meetingList");
	}	
	
	

}

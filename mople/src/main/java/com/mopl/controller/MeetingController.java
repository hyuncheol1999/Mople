package com.mopl.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.MeetingDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.model.MeetingDTO;
import com.mopl.model.RegularMeetingDTO;
import com.mopl.model.SessionInfo;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.annotation.ResponseBody;
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
	public ModelAndView meetingDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 정모장인지아닌지 확인
		ModelAndView mav = new ModelAndView("meeting/meetingDetail");
		MeetingDAO dao = new MeetingDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String param = req.getParameter("meetingIdx");
		if (param != null && !param.isEmpty()) {
			Long meetingIdx = Long.parseLong(param);
			mav.addObject("meetingIdx", meetingIdx);
			
			if (info != null) {
				try {
					RegularMeetingDAO rDao = new RegularMeetingDAO();
					boolean isLeader = rDao.isMeetingMember(meetingIdx, info.getMemberIdx());
					mav.addObject("isLeader", isLeader);
				} catch (Exception e) {
					e.printStackTrace();
					mav.addObject("isLeader", false);
				}
			} else {
				mav.addObject("isLeader", false);
			}
		} else {
			System.out.println("meetingIdx 존재X");
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
		public ModelAndView meetingSchedule(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("meeting/meetingSchedule");
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			try {
				String meetingIdxParam = req.getParameter("meetingIdx");
				if (meetingIdxParam == null || meetingIdxParam.trim().isEmpty()) {
					System.out.println("meetingIdx 파라미터 없음");
					return new ModelAndView("redirect:/");
				}

				Long meetingIdx = Long.parseLong(meetingIdxParam);
				Long memberIdx = (info != null) ? info.getMemberIdx() : 0;

				RegularMeetingDAO dao = new RegularMeetingDAO();

				boolean isLogin = (info != null);
				boolean meetingMember = isLogin && dao.isMeetingMember(meetingIdx, memberIdx);
				boolean isLeader = meetingMember; 

				List<RegularMeetingDTO> list = dao.listSchedule(meetingIdx);
				for (RegularMeetingDTO dto : list) {
					boolean joined = (memberIdx != 0) && dao.isJoined(dto.getRegularMeetingIdx(), memberIdx);
					dto.setJoined(joined);
				}

				mav.addObject("isLogin", isLogin);
				mav.addObject("meetingMember", meetingMember);
				mav.addObject("isLeader", isLeader);
				mav.addObject("scheduleList", list);
				mav.addObject("meetingIdx", meetingIdx);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return mav;
		}

		@RequestMapping(value = "/schedule/join", method = RequestMethod.POST)
		@ResponseBody
		// 정모 참여
		public Map<String,Object> joinSchedule(HttpServletRequest req, HttpServletResponse resp){
		    HttpSession session = req.getSession();
		    SessionInfo info  = (SessionInfo) session.getAttribute("member");
		    Map<String,Object> map = new HashMap<>();

		    String idxParam = req.getParameter("regularMeetingIdx");
		    if(info == null || idxParam == null || idxParam.isBlank()){
		        map.put("success", false); 
		        return map;
		    }

		    try {
		        long regularMeetingIdx = Long.parseLong(idxParam);
		        RegularMeetingDAO dao  = new RegularMeetingDAO();
		        dao.insertParticipant(regularMeetingIdx, info.getMemberIdx());
		        map.put("success", true);
		    } catch (Exception e) {
		        e.printStackTrace();
		        map.put("success", false);
		    }
		    return map;
		}


		@RequestMapping(value = "/schedule/cancel", method = RequestMethod.POST)
		@ResponseBody
		// 정모 참여 취소 
		public Map<String, Object> cancelSchedule(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			Map<String, Object> map = new HashMap<>();
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info == null) {
				map.put("success", false);
				return map;
			}

			try {
				long regularMeetingIdx = Long.parseLong(req.getParameter("regularMeetingIdx"));
				RegularMeetingDAO dao = new RegularMeetingDAO();
				dao.deleteParticipant(regularMeetingIdx, info.getMemberIdx());
				
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
			}
			return map;
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

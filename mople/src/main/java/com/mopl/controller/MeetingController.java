package com.mopl.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.MeetingDAO;
import com.mopl.dao.MemberOfMeetingDAO;
import com.mopl.dao.RegionCategoryDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.dao.SportCategoryDAO;
import com.mopl.model.MeetingDTO;
import com.mopl.model.MemberOfMeetingDTO;
import com.mopl.model.RegionCategoryDTO;
import com.mopl.model.RegularMeetingDTO;
import com.mopl.model.SessionInfo;
import com.mopl.model.SportCategoryDTO;
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
public class MeetingController {
	// 모임 리스트
	@RequestMapping(value = "/meeting/meetingList", method = RequestMethod.GET)
	public ModelAndView meetingList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingList");
		
		MeetingDAO dao = new MeetingDAO();
		SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
		RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			
			// 처음 접속
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int sportCategory = Integer.parseInt(req.getParameter("sportCategory"));
			int regionCategory = Integer.parseInt(req.getParameter("regionCategory"));
			String sortBy = req.getParameter("sortBy");
			
			int dataCount;
			if(sportCategory == 0 && regionCategory == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(sportCategory, regionCategory);
			}
			
			int size = 18;
			int total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<MeetingDTO> list = dao.findAllMeetings(offset, size, sportCategory, regionCategory, sortBy);
			
			List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
			List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();
			
			String query = "sportCategory=" + sportCategory + "&regionCategory=" + regionCategory + "&sortBy=" + sortBy;
			
			String cp = req.getContextPath();
			
			String listUrl = cp + "/meeting/meetingList?" + query;
			String articleUrl = cp + "/meeting/meetingDetail?page=" + current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			mav.addObject("list", list);
			mav.addObject("sportCategoryList", sportCategoryList);
			mav.addObject("regionCategoryList", regionCategoryList);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			
			mav.addObject("paging", paging);
			
			mav.addObject("sportCategory", sportCategory);
			mav.addObject("regionCategory", regionCategory);
			mav.addObject("sortBy", sortBy);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}	
	
	// 모임 리스트 - AJAX:TEXT
	@RequestMapping(value = "/meeting/meetingLayout", method = RequestMethod.GET)
	public ModelAndView meetingLayout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingLayout");
		
		MeetingDAO dao = new MeetingDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			
			// 처음 접속
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int sportCategory = Integer.parseInt(req.getParameter("sportCategory"));
			int regionCategory = Integer.parseInt(req.getParameter("regionCategory"));
			String sortBy = req.getParameter("sortBy");
			
			int dataCount;
			if(sportCategory == 0 && regionCategory == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(sportCategory, regionCategory);
			}
			
			int size = 18;
			int total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<MeetingDTO> list = dao.findAllMeetings(offset, size, sportCategory, regionCategory, sortBy);
			
			for(MeetingDTO dto : list) {
				dto.setMeetingName(util.htmlSymbols(dto.getMeetingName()));
				dto.setContent(util.htmlSymbols(dto.getContent()));
			}
			
			String query = "sportCategory=" + sportCategory + "&regionCategory=" + regionCategory + "&sortBy=" + sortBy;
			
			String cp = req.getContextPath();
			
			String articleUrl = cp + "/meeting/meetingDetail?page=" + current_page + "&" + query;
			
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			
			mav.addObject("paging", paging);
			
			mav.addObject("sportCategory", sportCategory);
			mav.addObject("regionCategory", regionCategory);
			mav.addObject("sortBy", sortBy);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
		MemberOfMeetingDAO memberOfMeetingDao = new MemberOfMeetingDAO();
		
		MeetingDTO meetingDto = null;
		List<MemberOfMeetingDTO> memberOfMeetingList = null;
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		// meetingIdx 파라미터가 없을 경우
		String param = req.getParameter("meetingIdx");
		if (param == null) {
			return new ModelAndView("meeting/deletedMeeting");
		}
		
		if (param != null && !param.isEmpty()) {
			// 모임 정보
			Long meetingIdx = Long.parseLong(param);
			meetingDto = dao.findByMeeetingIdx(meetingIdx);
			memberOfMeetingList = memberOfMeetingDao.findMeetingIdx(meetingIdx);
			
			mav.addObject("meetingIdx", meetingIdx);
			mav.addObject("meetingName", meetingDto.getMeetingName());
			mav.addObject("sportName", meetingDto.getSportName());
			mav.addObject("regionName", meetingDto.getRegionName());
			mav.addObject("currentMembers", meetingDto.getCurrentMembers());
			mav.addObject("meetingProfilePhoto", meetingDto.getMeetingProfilePhoto());

			mav.addObject("memberOfMeetingList", memberOfMeetingList);
			
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
		MeetingDAO meetingDao = new MeetingDAO();
		MemberOfMeetingDAO memberOfMeetingDao = new MemberOfMeetingDAO();
		MeetingDTO meetingDto = null;
		List<MemberOfMeetingDTO> memberOfMeetingList = null;
		
		try {
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			meetingDto = meetingDao.findByMeeetingIdx(meetingIdx);
			memberOfMeetingList = memberOfMeetingDao.findMeetingIdx(meetingIdx);
			
			mav.addObject("meetingDesc", meetingDto.getMeetingDesc());
			mav.addObject("regionName", meetingDto.getRegionName());
			mav.addObject("currentMembers", meetingDto.getCurrentMembers());
			
			mav.addObject("memberOfMeetingList", memberOfMeetingList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "meetingProfilePhoto";
		
		try {
			MeetingDTO dto = new MeetingDTO();
			dto.setMeetingName(req.getParameter("meetingName"));
			dto.setMeetingDesc(req.getParameter("meetingDesc"));
			dto.setSportIdx(Integer.parseInt(req.getParameter("sportCategoryNo")));
			dto.setRegionIdx(Integer.parseInt(req.getParameter("regionCategoryNo")));
			
			dto.setMemberIdx(info.getMemberIdx());
			dto.setRole(0);
			
			Part p = req.getPart("meetingProfilePhoto");
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			
			if(multiFile != null) {
				dto.setMeetingProfilePhoto(multiFile.getSaveFilename());
			}
			
			dao.insertMeeting(dto);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/meeting/meetingList");
	}	
	
	@RequestMapping(value = "/meeting/deletedMeeting", method = RequestMethod.GET)
	public ModelAndView deletedMeeting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("redirect:/meeting/deletedMeeting");
	}
	


}

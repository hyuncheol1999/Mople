package com.mopl.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mopl.dao.BungaeMeetingDAO;
import com.mopl.dao.MemberOfBungaeMeetingDAO;
import com.mopl.dao.RegionCategoryDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.dao.SportCategoryDAO;
import com.mopl.model.BungaeMeetingDTO;
import com.mopl.model.MeetingDTO;
import com.mopl.model.MemberOfBungaeMeetingDTO;
import com.mopl.model.RegionCategoryDTO;
import com.mopl.model.RegularMeetingDTO;
import com.mopl.model.SessionInfo;
import com.mopl.model.SportCategoryDTO;
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
public class BungaeMeetingController {
	/*
		// 번개모임 전용 리스트 페이지
		@RequestMapping(value = "/bungaeMeeting/bungaeMeetingList", method = RequestMethod.GET)
		public ModelAndView bungaeMeetingList(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingList");

			BungaeMeetingDAO dao = new BungaeMeetingDAO();
			SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
			RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
			MyUtil util = new MyUtil();

			try {
				String page = req.getParameter("page");
				int current_page = 1;

				// 처음 접속
				if (page != null) {
					current_page = Integer.parseInt(page);
				}

				int sportCategory = Integer.parseInt(req.getParameter("sportCategory"));
				int regionCategory = Integer.parseInt(req.getParameter("regionCategory"));
				String sortBy = req.getParameter("sortBy");

				int dataCount;
				if (sportCategory == 0 && regionCategory == 0) {
					dataCount = dao.dataCount();
				} else {
					dataCount = dao.dataCount(sportCategory, regionCategory);
				}

				int size = 18;
				int total_page = util.pageCount(dataCount, size);

				if (current_page > total_page) {
					current_page = total_page;
				}

				int offset = (current_page - 1) * size;
				if (offset < 0)
					offset = 0;

				List<BungaeMeetingDTO> list = dao.findAllBungaeMeetings(offset, size, sportCategory, regionCategory, sortBy);

				List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
				List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

				String query = "sportCategory=" + sportCategory + "&regionCategory=" + regionCategory + "&sortBy=" + sortBy;

				String cp = req.getContextPath();

				String listUrl = cp + "/bungaeMeeting/bungaeMeetingList?" + query;
				String articleUrl = cp + "/bungaeMeeting/bungaeMeetingDetail?page=" + current_page + "&" + query;

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
		*/
	// 번개모임 생성 폼
		@RequestMapping(value = "/bungaeMeeting/bungaeMeetingCreate", method = RequestMethod.GET)
		public ModelAndView meetingCreateForm(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingCreate");
			SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
			RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();

			try {
				List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
				List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

				mav.addObject("sportCategoryList", sportCategoryList);
				mav.addObject("regionCategoryList", regionCategoryList);
				mav.addObject("mode", "bungaeMeetingCreate");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mav;
		}


		// 번개모임 생성
		@RequestMapping(value = "/bungaeMeeting/bungaeMeetingCreate", method = RequestMethod.POST)
		public ModelAndView meetingCreateSubmit(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BungaeMeetingDAO dao = new BungaeMeetingDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			try {
				BungaeMeetingDTO dto = new BungaeMeetingDTO();
				dto.setSubject(req.getParameter("subject"));
				dto.setContent(req.getParameter("content"));
				dto.setStartDate(req.getParameter("startDate"));
				dto.setStartDate(req.getParameter("endDate"));
				dto.setPlace(req.getParameter("place"));
				dto.setCapacity(Integer.parseInt(req.getParameter("capacity")));
				
				dto.setBungaeMemberIdx(info.getMemberIdx());
				
				dao.insertBungaeMeeting(dto);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ModelAndView("redirect:/bungaeMeeting/home?sportCategory=0&regionCategory=0");
		}
		
		// 번개모임 수정 폼
		@RequestMapping(value = "/bungaemeeting/bungaeMeetingUpdate", method = RequestMethod.GET)
		public ModelAndView meetingUpdateForm(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("bungaemeeting/bungaeMeetingCreate");
			SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
			RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
			BungaeMeetingDAO dao = new BungaeMeetingDAO();

			try {
				long bungaeMeetingIdx = Long.parseLong(req.getParameter("bungaeMeetingIdx"));
				BungaeMeetingDTO dto = dao.findByBungaeMeetingIdx(bungaeMeetingIdx);
				
				List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
				List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

				mav.addObject("dto", dto);
				mav.addObject("sportCategoryList", sportCategoryList);
				mav.addObject("regionCategoryList", regionCategoryList);
				mav.addObject("mode", "bungaeMeetingUpdate");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mav;
		}
		
		// 번개모임 수정
		@RequestMapping(value = "/bungaemeeting/bungaeMeetingUpdate", method = RequestMethod.POST)
		public ModelAndView meetingUpdateSubmit(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			BungaeMeetingDAO dao = new BungaeMeetingDAO();

			String bungaeMeetingIdxParam = req.getParameter("bungaeMeetingIdx");
			try {
				BungaeMeetingDTO dto = new BungaeMeetingDTO();
				dto.setBungaeMeetingIdx(Long.parseLong(bungaeMeetingIdxParam));
				dto.setSubject(req.getParameter("subject"));
				dto.setContent(req.getParameter("content"));
				
				dao.updateBungaeMeeting(dto);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ModelAndView("redirect:/bungaeMeeting/bungaeMeetingDetail?sportCategory=0&regionCategory=0&bungaeMeetingIdx=" + bungaeMeetingIdxParam);
		}
		
		// 번개모임 신청 승인
		@ResponseBody
		@RequestMapping(value = "/bungaeMeeting/approve", method = RequestMethod.POST)
		public Map<String, Object> approveMember(HttpServletRequest req, HttpServletResponse resp) {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			Map<String, Object> map = new HashMap<>();
			
			String idxParam = req.getParameter("bungaeMeetingIdx");
			if (info == null || idxParam == null || idxParam.isBlank()) {
				map.put("success", false);
				return map;
			}
			
			try {
				MemberOfBungaeMeetingDAO dao = new MemberOfBungaeMeetingDAO();
				MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
				
				dto.setBungaeMeetingIdx(Long.parseLong(idxParam));
				dto.setMemberIdx(Long.parseLong(req.getParameter("memberIdx")));
				// 관리자: 0 / 회원: 1 / 대기: 2
				dto.setRole(1);
				
				dao.updateMemberRole(dto); 
				
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
			}
			return map;
		}
		
		// 번개모임 신청 거절
		@ResponseBody
		@RequestMapping(value = "/bungaeMeeting/reject", method = RequestMethod.POST)
		public Map<String, Object> rejectMember(HttpServletRequest req, HttpServletResponse resp) {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			Map<String, Object> map = new HashMap<>();
			
			String idxParam = req.getParameter("bungaeMeetingIdx");
			if (info == null || idxParam == null || idxParam.isBlank()) {
				map.put("success", false);
				return map;
			}
			
			try {
				MemberOfBungaeMeetingDAO dao = new MemberOfBungaeMeetingDAO();
				MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
				
				dto.setBungaeMeetingIdx(Long.parseLong(idxParam));
				dto.setMemberIdx(Long.parseLong(req.getParameter("memberIdx")));

				dao.leaveMeeting(dto);
				
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
			}
			return map;
		}
		
		// 홈화면: 정기모임 임박 + 이번주 번개모임 표시
		@RequestMapping(value = "/bungaeMeeting/bungaeMeetingList", method = RequestMethod.GET)
		public ModelAndView bungaeMeetingHome(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingList");

			RegularMeetingDAO rDao = new RegularMeetingDAO();
			BungaeMeetingDAO bmDao = new BungaeMeetingDAO();
			MemberOfBungaeMeetingDAO mbmDao = new MemberOfBungaeMeetingDAO();
			SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
			RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
			MyUtil util = new MyUtil();
			
			String keyword = req.getParameter("search");
			if (keyword == null) keyword = "";

			try {
				String page = req.getParameter("page");
				int current_page = 1;

				// 처음 접속
				if (page != null) {
					current_page = Integer.parseInt(page);
				}
				
				int sportCategory = Integer.parseInt(req.getParameter("sportCategory"));
				int regionCategory = Integer.parseInt(req.getParameter("regionCategory"));
				String sortBy = req.getParameter("sortBy");
				
				int dataCount;
				if (sportCategory == 0 && regionCategory == 0) {
					dataCount = bmDao.dataCount();
				} else {
					dataCount = bmDao.dataCount(sportCategory, regionCategory);
				}

				int size = 18;
				int total_page = util.pageCount(dataCount, size);

				if (current_page > total_page) {
					current_page = total_page;
				}

				int offset = (current_page - 1) * size;
				if (offset < 0)
					offset = 0;

				List<RegularMeetingDTO> urgentRegularMeetings = rDao.selectUrgentRegularMeetings(keyword);
				List<BungaeMeetingDTO> weeklyBungaeMeetings = bmDao.selectWeeklyBungaeMeetings(keyword);
				
				for (RegularMeetingDTO dto : urgentRegularMeetings) {
					dto.setSubject(util.htmlSymbols(dto.getSubject()));
					dto.setStartDate(util.htmlSymbols(dto.getStartDate()));				
					dto.setPlace(util.htmlSymbols(dto.getPlace()));
					dto.setCurrentCnt(mbmDao.findMemberCount(dto.getRegularMeetingIdx()));
				}
				
				for(BungaeMeetingDTO dto : weeklyBungaeMeetings) {
					dto.setSubject(util.htmlSymbols(dto.getSubject()));
					dto.setContent(util.htmlSymbols(dto.getContent()));
					dto.setStartDate(util.htmlSymbols(dto.getStartDate()));
					dto.setEndDate(util.htmlSymbols(dto.getEndDate()));
					dto.setPlace(util.htmlSymbols(dto.getPlace()));
					dto.setCapacity(dto.getCapacity());
					
				}
				
				List<String> weekDates = new ArrayList<>();
				LocalDate today = LocalDate.now();
				for (int i = 0; i < 7; i++) {
					weekDates.add(today.plusDays(i).toString());
				}
				
				List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
				List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

				String query = "sportCategory=" + sportCategory + "&regionCategory=" + regionCategory + "&sortBy=" + sortBy;

				String cp = req.getContextPath();

				String listUrl = cp + "/meeting/meetingList?" + query;
				String articleUrl = cp + "/meeting/meetingDetail?page=" + current_page + "&" + query;

				String paging = util.paging(current_page, total_page, listUrl);

				mav.addObject("list", urgentRegularMeetings);
				mav.addObject("list2", weeklyBungaeMeetings);
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

				
				mav.addObject("urgentRegularMeetings", urgentRegularMeetings);
				mav.addObject("weeklyBungaeMeetings", weeklyBungaeMeetings);
				mav.addObject("weekDates", weekDates);
				mav.addObject("search", keyword);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mav;
		}
}


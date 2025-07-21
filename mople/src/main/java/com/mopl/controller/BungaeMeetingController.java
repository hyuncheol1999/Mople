package com.mopl.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mopl.dao.BungaeMeetingDAO;
import com.mopl.dao.BungaeReplyDAO;
import com.mopl.dao.MemberOfBungaeMeetingDAO;
import com.mopl.dao.RegionCategoryDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.dao.SportCategoryDAO;
import com.mopl.model.BungaeMeetingDTO;
import com.mopl.model.BungaeReplyDTO;
import com.mopl.model.MemberOfBungaeMeetingDTO;
import com.mopl.model.RegionCategoryDTO;
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
	@RequestMapping(value = "/bungaeMeeting/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingHome");

		String keyword = defaultString(req.getParameter("q"), "");
		String searchType = defaultString(req.getParameter("searchType"), "all");

		RegularMeetingDAO regularMeetingDAO = new RegularMeetingDAO();
		BungaeMeetingDAO bungaeMeetingDAO = new BungaeMeetingDAO();

		try {
			var urgentRegularMeetings = regularMeetingDAO.selectUrgentRegularMeetings(keyword);
			var weeklyBungaeMeetings = bungaeMeetingDAO.selectWeeklyBungaeMeetings(keyword, searchType);

			mav.addObject("urgentRegularMeetings", urgentRegularMeetings);
			mav.addObject("weeklyBungaeMeetings", weeklyBungaeMeetings);
			mav.addObject("dowFormatterPattern", "E");
			mav.addObject("q", keyword);
			mav.addObject("search", keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	// 2) 번개모임 전체 리스트
	@RequestMapping(value = "/bungaeMeeting/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingList");

		BungaeMeetingDAO dao = new BungaeMeetingDAO();
		SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
		RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
		MyUtil util = new MyUtil();

		try {
			int currentPage = parseIntOrDefault(req.getParameter("page"), 1);
			int sportCategory = parseIntOrDefault(req.getParameter("sportCategory"), 0);
			int regionCategory = parseIntOrDefault(req.getParameter("regionCategory"), 0);
			String sortBy = defaultString(req.getParameter("sortBy"), "latest");

			int dataCount = (sportCategory == 0 && regionCategory == 0) ? dao.dataCount()
					: dao.dataCount(sportCategory, regionCategory);

			int size = 18;
			int totalPage = util.pageCount(dataCount, size);
			if (totalPage == 0)
				totalPage = 1;
			if (currentPage > totalPage)
				currentPage = totalPage;

			int offset = (currentPage - 1) * size;
			if (offset < 0)
				offset = 0;

			List<BungaeMeetingDTO> list = dao.findAllBungaeMeetings(offset, size, sportCategory, regionCategory,
					sortBy);

			for (BungaeMeetingDTO dto : list) {
				dto.setSubject(util.htmlSymbols(dto.getSubject()));
				dto.setContent(util.htmlSymbols(dto.getContent()));
			}

			List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
			List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

			String query = "sportCategory=" + sportCategory + "&regionCategory=" + regionCategory + "&sortBy=" + sortBy;
			String cp = req.getContextPath();

			String listUrl = cp + "/bungaeMeeting/bungaeMeetingDetail?" + query;
			String detailUrl = cp + "/bungaeMeeting/detail?page=" + currentPage + "&" + query;
			String paging = util.paging(currentPage, totalPage, listUrl);

			mav.addObject("list", list);
			mav.addObject("sportCategoryList", sportCategoryList);
			mav.addObject("regionCategoryList", regionCategoryList);

			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", currentPage);
			mav.addObject("total_page", totalPage);
			mav.addObject("articleUrl", detailUrl);
			mav.addObject("paging", paging);

			mav.addObject("sportCategory", sportCategory);
			mav.addObject("regionCategory", regionCategory);
			mav.addObject("sortBy", sortBy);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

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
	public ModelAndView meetingCreateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

		HttpSession session = req.getSession(false);
		SessionInfo info = (session != null) ? (SessionInfo) session.getAttribute("member") : null;
		if (info == null)
			throw new ServletException("LOGIN_REQUIRED");

		String subject = req.getParameter("subject");
		String content = req.getParameter("content");
		String startRaw = req.getParameter("startDate");
		String endRaw = req.getParameter("endDate");
		String place = req.getParameter("place");
		String capacityS = req.getParameter("capacity");
		String catS = req.getParameter("categoryIdx");
		String regS = req.getParameter("regionIdx");

		java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		java.time.LocalDateTime start, end;
		try {
			start = java.time.LocalDateTime.parse(startRaw, fmt);
			end = java.time.LocalDateTime.parse(endRaw, fmt);
		} catch (Exception pe) {
			pe.printStackTrace();
			throw new ServletException("DATETIME_PARSE_FAIL", pe);
		}
		if (end.isBefore(start))
			throw new ServletException("END_BEFORE_START");

		int capacity, categoryIdx, regionIdx;
		try {
			capacity = Integer.parseInt(capacityS);
			categoryIdx = Integer.parseInt(catS);
			regionIdx = Integer.parseInt(regS);
		} catch (NumberFormatException nfe) {
			throw new ServletException("NUMBER_PARSE_FAIL", nfe);
		}

		BungaeMeetingDTO dto = new BungaeMeetingDTO();
		dto.setSubject(subject);
		dto.setContent(content);
		dto.setStartDate(start);
		dto.setEndDate(end);
		dto.setPlace(place);
		dto.setCapacity(capacity);
		dto.setCategoryIdx(categoryIdx);
		dto.setRegionIdx(regionIdx);
		dto.setBungaeMemberIdx(info.getMemberIdx());

		try {
			new BungaeMeetingDAO().insertBungaeMeeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/bungaeMeeting/home");
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
		long bungaeMeetingIdx = Long.parseLong(req.getParameter("bungaeMeetingIdx"));

		try {
			BungaeMeetingDTO dto = new BungaeMeetingDTO();
			dto = dao.findByBungaeMeetingIdx(bungaeMeetingIdx);

			dao.updateBungaeMeeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView(
				"redirect:/bungaeMeeting/bungaeMeetingDetail?sportCategory=0&regionCategory=0&bungaeMeetingIdx="
						+ bungaeMeetingIdx);
	}

	// 번개모임 신청 승인
	@ResponseBody
	@RequestMapping(value = "/bungaeMeeting/approve", method = RequestMethod.POST)
	public Map<String, Object> approveMember(HttpServletRequest req, HttpServletResponse resp) {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    Map<String, Object> map = new HashMap<>();

	    try {
	        String idxParam = req.getParameter("bungaeMeetingIdx");
	        String memberIdxParam = req.getParameter("memberIdx");

	        if (info == null || idxParam == null || memberIdxParam == null || idxParam.isBlank() || memberIdxParam.isBlank()) {
	            map.put("success", false);
	            return map;
	        }

	        long bungaeMeetingIdx = Long.parseLong(idxParam);
	        long memberIdx = Long.parseLong(memberIdxParam);

	        MemberOfBungaeMeetingDAO dao = new MemberOfBungaeMeetingDAO();
	        if (!dao.isLeader(bungaeMeetingIdx, info.getMemberIdx())) {
	            map.put("success", false);
	            return map;
	        }

	        MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
	        dto.setBungaeMeetingIdx(bungaeMeetingIdx);
	        dto.setMemberIdx(memberIdx);
	        dto.setRole(1);

	        dao.updateMemberRole(dto);

	        BungaeMeetingDAO mDao = new BungaeMeetingDAO();

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

	// 번개모임 상세
	@RequestMapping(value = "/bungaeMeeting/detail", method = RequestMethod.GET)
	public ModelAndView meetingDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 번개장인지아닌지 확인
		ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeMeetingDetail");
		BungaeMeetingDAO dao = new BungaeMeetingDAO();
		MemberOfBungaeMeetingDAO mDao = new MemberOfBungaeMeetingDAO();

		BungaeMeetingDTO bungaeMeetingDto = null;

		// 유저의 번개모임 참가 여부
		String userStatus = "NOT_LOGIN";

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			// bungaeMeetingIdx 파라미터가 없을 경우
			String param = req.getParameter("bungaeMeetingIdx");
			if (param == null) {
				return new ModelAndView("meeting/deletedMeeting");
			}

			if (param != null && !param.isEmpty()) {
				// 번개모임 정보
				Long bungaeMeetingIdx = Long.parseLong(param);
				bungaeMeetingDto = dao.findByBungaeMeetingIdx(bungaeMeetingIdx);

				mav.addObject("bungaeMeetingIdx", bungaeMeetingIdx);
				mav.addObject("subject", bungaeMeetingDto.getSubject());
				mav.addObject("place", bungaeMeetingDto.getPlace());
				mav.addObject("startDate", bungaeMeetingDto.getStartDate());
				mav.addObject("content", bungaeMeetingDto.getContent());
				mav.addObject("sportName", bungaeMeetingDto.getSportName());
				mav.addObject("regionName", bungaeMeetingDto.getRegionName());
				// 대기인원 포함
				mav.addObject("currentCnt", bungaeMeetingDto.getCurrentCnt());

				if (info != null) {
					userStatus = "NOT_JOINED";

					MemberOfBungaeMeetingDAO mobDao = new MemberOfBungaeMeetingDAO();
					boolean isLeader = mobDao.isLeader(bungaeMeetingIdx, info.getMemberIdx());

					if (isLeader) {
						userStatus = "HOST";
					} else if (mobDao.isWaiting(bungaeMeetingIdx, info.getMemberIdx())) {
						userStatus = "WAITING";
					}

					mav.addObject("isLeader", isLeader);
					mav.addObject("memberIdx", info.getMemberIdx());
				}

				// 대기인원 제외 카운트
				mav.addObject("memberCnt", mDao.findMemberCount(bungaeMeetingIdx));
				mav.addObject("userStatus", userStatus);

			} else {
				System.out.println("meetingIdx 존재X");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	// 번개에서 정모 참여
	@ResponseBody
	@RequestMapping(value = "/meetingAll/join", method = RequestMethod.POST)
	public Map<String, Object> joinjungmo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = request.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			result.put("status", "unauthorized");
			return result;
		}

		String meetingType = request.getParameter("type");
		long scheduleId = Long.parseLong(request.getParameter("scheduleId"));

		try {
			boolean joined = false;
			int count = 0;

			if ("regular".equals(meetingType)) {
				RegularMeetingDAO dao = new RegularMeetingDAO();
				BungaeMeetingDAO bDao = new BungaeMeetingDAO();
				joined = bDao.changeJoin(scheduleId, info.getMemberIdx());
				count = dao.findByRegularMeetingIdx(scheduleId).getCurrentCnt();
			} else if ("bungae".equals(meetingType)) {
				BungaeMeetingDAO dao = new BungaeMeetingDAO();
				joined = dao.changeJoin(scheduleId, info.getMemberIdx());
				count = dao.findByBungaeMeetingIdx(scheduleId).getCurrentCnt();
			}

			result.put("status", joined ? "joined" : "cancelled");
			result.put("currentCount", count);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "error");
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/bungaeMeeting/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<>();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		BungaeReplyDAO dao = new BungaeReplyDAO();

		String state = "false";

		try {
			BungaeReplyDTO dto = new BungaeReplyDTO();

			long bungaeMeetingIdx = Long.parseLong(req.getParameter("bungaeMeetingIdx"));
			dto.setBungaeMeetingIdx(bungaeMeetingIdx);
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

	@RequestMapping(value = "/bungaeMeeting/replyList", method = RequestMethod.GET)
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("bungaeMeeting/bungaeListReply");

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		BungaeReplyDAO dao = new BungaeReplyDAO();
		MyUtil util = new MyUtil();

		try {
			long bungaeMeetingIdx = Long.parseLong(req.getParameter("bungaeMeetingIdx"));
			int current_page = Integer.parseInt(req.getParameter("pageNo"));

			int size = 5;
			int replyCount = dao.dataCountReply(bungaeMeetingIdx, info.getMemberIdx(), info.getRole());
			int total_page = util.pageCount(replyCount, size);
			if (current_page > total_page)
				current_page = total_page;

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<BungaeReplyDTO> listReply = dao.listReply(bungaeMeetingIdx, offset, size, info.getMemberIdx(),
					info.getRole());
			for (BungaeReplyDTO dto : listReply) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}

			String paging = util.pagingMethod(current_page, total_page, "listReply");
			boolean isLeader = new MemberOfBungaeMeetingDAO().isLeader(bungaeMeetingIdx, info.getMemberIdx());
			
			mav.addObject("listReply", listReply);
			mav.addObject("pageNo", current_page);
			mav.addObject("replyCount", replyCount);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			
			mav.addObject("isLeader", isLeader);
			mav.addObject("memberIdx", info.getMemberIdx());
			mav.addObject("bungaeMeetingIdx", bungaeMeetingIdx);

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
		}

		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/bungaeMeeting/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<>();
		BungaeReplyDAO dao = new BungaeReplyDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

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

	private int parseIntOrDefault(String param, int def) {
		try {
			return (param == null || param.isBlank()) ? def : Integer.parseInt(param);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private String defaultString(String val, String def) {
		return (val == null) ? def : val;
	}
}
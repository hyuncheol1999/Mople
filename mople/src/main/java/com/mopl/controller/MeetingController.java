package com.mopl.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mopl.dao.MeetingAlbumDAO;
import com.mopl.dao.MeetingBoardDAO;
import com.mopl.dao.MeetingDAO;
import com.mopl.dao.MemberOfMeetingDAO;
import com.mopl.dao.RegionCategoryDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.dao.SportCategoryDAO;
import com.mopl.model.MeetingAlbumDTO;
import com.mopl.model.MeetingBoardDTO;
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
	public ModelAndView meetingList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingList");

		MeetingDAO dao = new MeetingDAO();
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

			List<MeetingDTO> list = dao.findAllMeetings(offset, size, sportCategory, regionCategory, sortBy);
			
			for (MeetingDTO dto : list) {
				dto.setMeetingName(util.htmlSymbols(dto.getMeetingName()));
				dto.setContent(util.htmlSymbols(dto.getContent()));				
			}

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
	public ModelAndView meetingLayout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingLayout");

		MeetingDAO dao = new MeetingDAO();
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

			List<MeetingDTO> list = dao.findAllMeetings(offset, size, sportCategory, regionCategory, sortBy);

			for (MeetingDTO dto : list) {
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
	@RequestMapping(value = "/meeting/meetingDetail", method = RequestMethod.GET)
	public ModelAndView meetingDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 정모장인지아닌지 확인
		ModelAndView mav = new ModelAndView("meeting/meetingDetail");
		MeetingDAO dao = new MeetingDAO();

		MeetingDTO meetingDto = null;

		// 유저의 모임 참가 여부
		String userStatus = "NOT_LOGIN";

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			// meetingIdx 파라미터가 없을 경우
			String param = req.getParameter("meetingIdx");
			if (param == null) {
				return new ModelAndView("meeting/deletedMeeting");
			}

			if (param != null && !param.isEmpty()) {
				// 모임 정보
				Long meetingIdx = Long.parseLong(param);
				meetingDto = dao.findByMeeetingIdx(meetingIdx);

				mav.addObject("meetingIdx", meetingIdx);
				mav.addObject("meetingName", meetingDto.getMeetingName());
				mav.addObject("sportName", meetingDto.getSportName());
				mav.addObject("regionName", meetingDto.getRegionName());
				// 대기인원 포함
				mav.addObject("currentMembers", meetingDto.getCurrentMembers());
				mav.addObject("meetingProfilePhoto", meetingDto.getMeetingProfilePhoto());

				if (info != null) {
					userStatus = "NOT_JOINED";
					
					MemberOfMeetingDAO momDao = new MemberOfMeetingDAO();
					// 모임 인원이면
					if(momDao.isMeetingMember(meetingIdx, info.getMemberIdx())) {
						userStatus = "JOINED";
						
						// 모임장이면
						if(momDao.isLeader(meetingIdx, info.getMemberIdx())) {
							userStatus = "HOST";
						} else if(momDao.isWaiting(meetingIdx, info.getMemberIdx())) {
							userStatus = "WAITING";
						}
					}
					
				}

				mav.addObject("userStatus", userStatus);

			} else {
				System.out.println("meetingIdx 존재X");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	// 모임상세 - 홈 화면
	@RequestMapping(value = "/meeting/meetingHome", method = RequestMethod.GET)
	public ModelAndView meetingHome(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("meeting/meetingHome");
		MeetingDAO meetingDao = new MeetingDAO();
		MemberOfMeetingDAO memberOfMeetingDao = new MemberOfMeetingDAO();
		MeetingBoardDAO meetingBoardDAO = new MeetingBoardDAO();
		MyUtil util = new MyUtil();
		
		MeetingDTO meetingDto = null;
		List<MemberOfMeetingDTO> memberOfMeetingList = null;
		List<MemberOfMeetingDTO> waitingList = null;
		List<MeetingBoardDTO> meetingBoardList = null;
		
		String userStatus = "NOT_LOGIN";
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			meetingDto = meetingDao.findByMeeetingIdx(meetingIdx);
			memberOfMeetingList = memberOfMeetingDao.findByMeetingIdx(meetingIdx);
			waitingList = memberOfMeetingDao.findWaitingList(meetingIdx);
			meetingBoardList = meetingBoardDAO.searchBoard(meetingIdx, 0, 5, null, null, null);

			meetingDto.setMeetingDesc(util.htmlSymbols(meetingDto.getMeetingDesc()));
			
			mav.addObject("meetingIdx", meetingIdx);
			mav.addObject("meetingDesc", meetingDto.getMeetingDesc());
			mav.addObject("regionName", meetingDto.getRegionName());
			mav.addObject("currentMembers", meetingDto.getCurrentMembers());
			
			String createDateString =  meetingDto.getCreatedDate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(createDateString);
			
			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy년 MM월 dd일 EEEE", Locale.KOREA);
			
			mav.addObject("createdDate", targetFormat.format(date));

			mav.addObject("memberOfMeetingList", memberOfMeetingList);
			mav.addObject("waitingList", waitingList);
			mav.addObject("meetingBoardList", meetingBoardList);
			
			
			if (info != null) {
				userStatus = "NOT_JOINED";
				
				mav.addObject("currentUserIdx", info.getMemberIdx());
				MemberOfMeetingDAO momDao = new MemberOfMeetingDAO();
				// 모임 인원이면
				if(momDao.isMeetingMember(meetingIdx, info.getMemberIdx())) {
					userStatus = "JOINED";
					
					// 모임장이면
					if(momDao.isLeader(meetingIdx, info.getMemberIdx())) {
						userStatus = "HOST";
					}
				}
				
			}

			mav.addObject("userStatus", userStatus);
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
			MemberOfMeetingDAO mDao = new MemberOfMeetingDAO();

			boolean isLogin = (info != null);
			boolean meetingMember = isLogin && mDao.isMeetingMember(meetingIdx, memberIdx);
			boolean isLeader = isLogin && mDao.isLeader(meetingIdx, memberIdx);

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
	public Map<String, Object> joinSchedule(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		Map<String, Object> map = new HashMap<>();

		String idxParam = req.getParameter("regularMeetingIdx");
		if (info == null || idxParam == null || idxParam.isBlank()) {
			map.put("success", false);
			return map;
		}

		try {
			long regularMeetingIdx = Long.parseLong(idxParam);
			RegularMeetingDAO dao = new RegularMeetingDAO();
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

	// 정모 생성 폼
	@RequestMapping(value = "/meeting/regularMeetingCreate", method = RequestMethod.GET)
	public ModelAndView regularMeetingForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mode = req.getParameter("mode");
		long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
		
		SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
		RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();

		ModelAndView mav = new ModelAndView("meeting/regularMeetingCreate");
		mav.addObject("mode", mode);
        mav.addObject("meetingIdx", meetingIdx);
	    
        if ("update".equals(mode)) {
        	long regularMeetingIdx = Long.parseLong(req.getParameter("regularMeetingIdx"));
        	RegularMeetingDTO dto = null;
        	
        	try {     		
                dto = new RegularMeetingDAO().findByRegularMeetingIdx(regularMeetingIdx);  
               
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	mav.addObject("dto", dto);
        } else {
        	List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
			List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

			mav.addObject("sportCategoryList", sportCategoryList);
			mav.addObject("regionCategoryList", regionCategoryList);

            mav.addObject("dto", new RegularMeetingDTO()); 
        }
        return mav;
    }

	// 정모 생성
	@RequestMapping(value = "/meeting/regularMeetingCreate", method = RequestMethod.POST)
	public ModelAndView regularMeetingCreateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mode = req.getParameter("mode");
        HttpSession session = req.getSession();
        long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));

        RegularMeetingDTO dto = new RegularMeetingDTO();
        dto.setMeetingIdx(meetingIdx);
        dto.setStartDate(req.getParameter("startDate"));
        dto.setPlace(req.getParameter("place"));
        dto.setCapacity(Integer.parseInt(req.getParameter("capacity")));
        dto.setSubject(req.getParameter("subject"));

        RegularMeetingDAO rDao = new RegularMeetingDAO();
        try {
            if ("update".equals(mode)) {
                dto.setRegularMeetingIdx(Long.parseLong(req.getParameter("regularMeetingIdx")));
                rDao.updateRegularMeeting(dto);
                 
            } else {
            	MeetingDAO mDao = new MeetingDAO();
                MeetingDTO mDto = mDao.getMeetingDetails(meetingIdx);
            	
                dto.setSportIdx(mDto.getSportIdx());
                dto.setRegionIdx(mDto.getRegionIdx());
                
                SessionInfo info = (SessionInfo) session.getAttribute("member");
                dto.setMemberIdx(info.getMemberIdx());
                
                rDao.createRegularMeeting(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/meeting/meetingDetail?sportCategory=0&regionCategory=0&meetingIdx=" + meetingIdx + "&afterCreate=true");
    }

	// 정모 삭제
	@RequestMapping(value = "/meeting/regularMeetingDelete", method = RequestMethod.POST)
	public String deleteRegularMeeting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
	    long regularMeetingIdx = Long.parseLong(req.getParameter("regularMeetingIdx"));

	    try {
	        new RegularMeetingDAO().deleteRegularMeeting(regularMeetingIdx);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "redirect:/meeting/meetingDetail?meetingIdx=" + meetingIdx;
	}

	@RequestMapping(value = "/regular/schedule", method = RequestMethod.GET)
	public String regularSchedule(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
	    req.setAttribute("meetingIdx", meetingIdx);

	    return "meeting/meetingDetail";

	}



	// 모임상세 - 사진첩
	@RequestMapping(value = "/meeting/meetingAlbum", method = RequestMethod.GET)
	public ModelAndView meetingAlbum(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingAlbum");
		MeetingAlbumDAO meetingAlbumDao = new MeetingAlbumDAO();
		MemberOfMeetingDAO memberOfMeetingDao = new MemberOfMeetingDAO();
		
		List<MemberOfMeetingDTO> memberOfMeetingList = null;
		List<MeetingAlbumDTO> meetingAlbumList = null;
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String userStatus = "NOT HOST";

		try {
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));

			memberOfMeetingList = memberOfMeetingDao.findByMeetingIdx(meetingIdx);
			meetingAlbumList = meetingAlbumDao.findByMeeetingIdx(meetingIdx);

			if(info != null) {
				if(memberOfMeetingDao.isLeader(meetingIdx, info.getMemberIdx())) {
					userStatus = "HOST";
				}
			}
			
			mav.addObject("meetingIdx", meetingIdx);
			mav.addObject("memberOfMeetingList", memberOfMeetingList);
			mav.addObject("meetingAlbumList", meetingAlbumList);
			mav.addObject("userStatus", userStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 모임 생성 폼
	@RequestMapping(value = "/meeting/meetingCreate", method = RequestMethod.GET)
	public ModelAndView meetingCreateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingCreate");
		SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
		RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
		MeetingDTO dto = new MeetingDTO();

		try {
			List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
			List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();
			
			mav.addObject("dto", dto);
			mav.addObject("sportCategoryList", sportCategoryList);
			mav.addObject("regionCategoryList", regionCategoryList);
			mav.addObject("mode", "meetingCreate");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 모임 생성
	@RequestMapping(value = "/meeting/meetingCreate", method = RequestMethod.POST)
	public ModelAndView meetingCreateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingDAO dao = new MeetingDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

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

			if (multiFile != null) {
				dto.setMeetingProfilePhoto(multiFile.getSaveFilename());
			}

			dao.insertMeeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meeting/meetingList?sportCategory=0&regionCategory=0");
	}

	// 모임 수정 폼
	@RequestMapping(value = "/meeting/meetingUpdate", method = RequestMethod.GET)
	public ModelAndView meetingUpdateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingCreate");
		SportCategoryDAO sportCategoryDao = new SportCategoryDAO();
		RegionCategoryDAO regionCategoryDao = new RegionCategoryDAO();
		MeetingDAO dao = new MeetingDAO();

		try {
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			MeetingDTO dto = dao.findByMeeetingIdx(meetingIdx);
			
			List<SportCategoryDTO> sportCategoryList = sportCategoryDao.findAllSportCategory();
			List<RegionCategoryDTO> regionCategoryList = regionCategoryDao.findAllRegionCategory();

			mav.addObject("dto", dto);
			mav.addObject("sportCategoryList", sportCategoryList);
			mav.addObject("regionCategoryList", regionCategoryList);
			mav.addObject("mode", "meetingUpdate");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	// 모임 수정
	@RequestMapping(value = "/meeting/meetingUpdate", method = RequestMethod.POST)
	public ModelAndView meetingUpdateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingDAO dao = new MeetingDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo) session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "meetingProfilePhoto";

		String meetingIdxParam = req.getParameter("meetingIdx");
		try {
			MeetingDTO beforeDto = dao.findByMeeetingIdx(Long.parseLong(meetingIdxParam));
			
			MeetingDTO dto = new MeetingDTO();
			dto.setMeetingIdx(Long.parseLong(meetingIdxParam));
			dto.setMeetingName(req.getParameter("meetingName"));
			dto.setMeetingDesc(req.getParameter("meetingDesc"));
			
			Part p = req.getPart("meetingProfilePhoto");
			
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);

			// 이미지 업로드 시
			if (multiFile != null) {
				// 파일 삭제
				fileManager.doFiledelete(pathname, beforeDto.getMeetingProfilePhoto());	
				
				dto.setMeetingProfilePhoto(multiFile.getSaveFilename());
			} else {
				dto.setMeetingProfilePhoto(null);
			}

			dao.updateMeeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meeting/meetingDetail?sportCategory=0&regionCategory=0&meetingIdx=" + meetingIdxParam);
	}
	
	// 모임 신청
	@ResponseBody
	@RequestMapping(value = "/meeting/join", method = RequestMethod.POST)
	public Map<String, Object> joinMeeting(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		Map<String, Object> map = new HashMap<>();

		String idxParam = req.getParameter("meetingIdx");
		if (info == null || idxParam == null || idxParam.isBlank()) {
			map.put("success", false);
			return map;
		}

		try {
			MemberOfMeetingDAO dao = new MemberOfMeetingDAO();
			MemberOfMeetingDTO dto = new MemberOfMeetingDTO();

			dto.setMeetingIdx(Long.parseLong(idxParam));
			dto.setMemberIdx(info.getMemberIdx());
			// 관리자: 0 / 회원: 1 / 대기: 2
			dto.setRole(2);

			dao.insertMeetingMember(dto);

			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	// 모임 신청 승인
	@ResponseBody
	@RequestMapping(value = "/meeting/approve", method = RequestMethod.POST)
	public Map<String, Object> approveMember(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		Map<String, Object> map = new HashMap<>();
		
		String idxParam = req.getParameter("meetingIdx");
		if (info == null || idxParam == null || idxParam.isBlank()) {
			map.put("success", false);
			return map;
		}
		
		try {
			MemberOfMeetingDAO dao = new MemberOfMeetingDAO();
			MemberOfMeetingDTO dto = new MemberOfMeetingDTO();
			
			dto.setMeetingIdx(Long.parseLong(idxParam));
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
	
	// 모임 탈퇴 / 모임 신청 거절
	@ResponseBody
	@RequestMapping(value = "/meeting/reject", method = RequestMethod.POST)
	public Map<String, Object> rejectMember(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		Map<String, Object> map = new HashMap<>();
		
		String idxParam = req.getParameter("meetingIdx");
		if (info == null || idxParam == null || idxParam.isBlank()) {
			map.put("success", false);
			return map;
		}
		
		try {
			MemberOfMeetingDAO dao = new MemberOfMeetingDAO();
			MemberOfMeetingDTO dto = new MemberOfMeetingDTO();
			
			dto.setMeetingIdx(Long.parseLong(idxParam));
			dto.setMemberIdx(Long.parseLong(req.getParameter("memberIdx")));

			dao.leaveMeeting(dto);
			
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}

	// 모임 상세 - 사진첩 - 등록 폼
	@RequestMapping(value = "/meeting/meetingAlbumUpload", method = RequestMethod.GET)
	public ModelAndView meetingAlbumForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingAlbumUpload");
		MemberOfMeetingDAO momDao = new MemberOfMeetingDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			
			mav.addObject("usetStatus", "NOT HOST");					

			if(info != null && momDao.isLeader(meetingIdx, info.getMemberIdx())) {
				mav.addObject("usetStatus", "HOST");
			}
			
			
			mav.addObject("mode", "insert");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}

	// 모임 상세 - 사진첩 - 사진 등록
	@RequestMapping(value = "/meeting/meetingAlbumUpload", method = RequestMethod.POST)
	public ModelAndView meetingAlbumSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MeetingAlbumDAO dao = new MeetingAlbumDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "meetingAlbum";
		String query = "meetingIdx=";

		try {
			MeetingAlbumDTO dto = new MeetingAlbumDTO();

			String meetingIdxStr = req.getParameter("meetingIdx");
			query += meetingIdxStr;

			dto.setMeetingIdx(Long.parseLong(meetingIdxStr));
			dto.setContent(req.getParameter("content"));

			// 사진 등록 멤버
			dto.setMemberIdx(info.getMemberIdx());
			
			Part p = req.getPart("meetingAlbumImage");
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);

			if (multiFile != null) {
				dto.setImageFileName(multiFile.getSaveFilename());
			}

			dao.insertAlbum(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/meeting/meetingDetail?" + query);
	}
	
	// 모임 상세 - 사진첩 - 사진 삭제
	@ResponseBody
	@RequestMapping(value = "/meeting/albumImageDelete", method = RequestMethod.POST)
	public Map<String, Object> albumImageDelete(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> map = new HashMap<>();
		FileManager fileManager = new FileManager();
		MeetingAlbumDAO maDao = new MeetingAlbumDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "meetingAlbum";
		
		if (info == null) {
			map.put("status", "false");
			return map;
		}
		
		try {
			long photoNum = Long.parseLong(req.getParameter("photoNum"));
			
			MeetingAlbumDTO dto = maDao.findByPhotoNum(photoNum);
			
			if(dto != null) {
				// 파일 삭제
				fileManager.doFiledelete(pathname, dto.getImageFileName());				

				// DB 삭제
				maDao.deleteImage(photoNum);
			}
			
			map.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "false");
		}
		return map;
	}

	// 모임 탈퇴 폼 - 모임장
	@RequestMapping(value = "/meeting/leaveLeader", method = RequestMethod.GET)
	public ModelAndView leaveLeader(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/leaveLeader");
		MemberOfMeetingDAO dao = new MemberOfMeetingDAO();
		mav.addObject("list", dao.findByMeetingIdx(Long.parseLong(req.getParameter("meetingIdx"))));
		
		return mav;
	}
	
	// 모임 탈퇴 - 모임장
	@ResponseBody
	@RequestMapping(value = "/meeting/leaveLeader", method = RequestMethod.POST)
	public ModelAndView leaveLeaderSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MemberOfMeetingDAO dao = new MemberOfMeetingDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String query = "sportCategory=0&regionCategory=0&sortBy=latest";
		
		try {
			MemberOfMeetingDTO dto = new MemberOfMeetingDTO();
			// 해체할 모임 번호
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			// 위임할 모임원 번호
			long memberIdx = Long.parseLong(req.getParameter("memberCategoryNo"));

			dto.setMeetingIdx(meetingIdx);
			dto.setMemberIdx(memberIdx);
			dto.setRole(0);
			
			dao.leaveLeader(dto, info.getMemberIdx());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/meeting/meetingList?" + query);
		
	}
	
	// 모임 해체
	@ResponseBody
	@RequestMapping(value = "/meeting/meetingDelete", method = RequestMethod.POST)
	public Map<String, Object> meetingDeleteSubmit2(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> map = new HashMap<>();
		MeetingDAO dao = new MeetingDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			if(info == null) {
				map.put("success", false);
			}
			
			long meetingIdx = Long.parseLong(req.getParameter("meetingIdx"));
			
			dao.deleteMeeting(meetingIdx);
			
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		
		return map;
	}
	
	
	
	// 이미 삭제된 모임 클릭시 페이지 전환
	@RequestMapping(value = "/meeting/deletedMeeting", method = RequestMethod.GET)
	public ModelAndView deletedMeeting(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return new ModelAndView("redirect:/meeting/deletedMeeting");
	}

}

package com.mopl.controller.admin;

import java.io.IOException;
import java.util.List;

import com.mopl.dao.MeetingDAO;
import com.mopl.dao.MemberDAO;
import com.mopl.dao.QnaDAO;
import com.mopl.dao.RegularMeetingDAO;
import com.mopl.model.RegularMeetingDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeManageController {
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView countQna(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QnaDAO Qdao = new QnaDAO();
		MeetingDAO MDao = new MeetingDAO();
		RegularMeetingDAO RDao = new RegularMeetingDAO();
		MemberDAO MemDao = new MemberDAO();
		
		ModelAndView mav = new ModelAndView("admin/home/main");
		
		try {
			int countQna = Qdao.countQna();
			int countTodayMember = MemDao.countTodayMember();
			int countMeeting = MDao.countMeeting(); 
			int countMember = MemDao.countMember();
			
			List<RegularMeetingDTO> list;
			list = RDao.getOngoingMeetingsTop5();
			
			mav.addObject("countQna", countQna);
			mav.addObject("countTodayMember", countTodayMember);
			mav.addObject("countMeeting", countMeeting);
			mav.addObject("countMember", countMember);
			mav.addObject("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}

}

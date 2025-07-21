package com.mopl.controller.admin;

import java.io.IOException;
import java.util.List;

import com.mopl.dao.MeetingDAO;
import com.mopl.model.MeetingDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingManagerController {
	@RequestMapping(value = "/admin/meeting/list", method = RequestMethod.GET)
	public ModelAndView meetingList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MeetingDAO dao = new MeetingDAO();
		
		ModelAndView mav = new ModelAndView("admin/meeting/list");
		
		try {
			List<MeetingDTO> list;
			list = dao.selectMeetingList();
			
			mav.addObject("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
}

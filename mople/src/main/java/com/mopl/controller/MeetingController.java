package com.mopl.controller;

import java.io.IOException;

import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingController {
	@RequestMapping(value = "/meeting/meetingList", method = RequestMethod.GET)
	public ModelAndView handleTest1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingList");
		
		return mav;
	}	

	@RequestMapping(value = "/meeting/meetingDetail", method = RequestMethod.GET)
	public ModelAndView handleTest2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingDetail");
		
		return mav;
	}	

	@RequestMapping(value = "/meeting/meetingHome", method = RequestMethod.GET)
	public ModelAndView handleTest3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingHome");
		
		return mav;
	}	
	
	@RequestMapping(value = "/meeting/meetingBbs", method = RequestMethod.GET)
	public ModelAndView handleTest4(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingBbs");
		
		return mav;
	}	
	
	@RequestMapping(value = "/meeting/meetingSchedule", method = RequestMethod.GET)
	public ModelAndView handleTest5(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingSchedule");
		
		return mav;
	}	

	@RequestMapping(value = "/meeting/meetingAlbum", method = RequestMethod.GET)
	public ModelAndView handleTest6(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/meetingAlbum");
		
		return mav;
	}	
	
	@RequestMapping(value = "/meeting/test", method = RequestMethod.GET)
	public ModelAndView handleTest7(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("meeting/test");
		
		return mav;
	}	
	

}

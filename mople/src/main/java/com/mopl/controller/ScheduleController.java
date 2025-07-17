package com.mopl.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mopl.crawler.ScheduleCrawler;
import com.mopl.model.GameDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ScheduleController {

	@RequestMapping(value = "/schedule/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("schedule/main");

		return mav;
	}

	@RequestMapping(value = "/schedule/matcharea", method = RequestMethod.GET)
	public ModelAndView baseball(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("schedule/matcharea");

		ScheduleCrawler sc = new ScheduleCrawler();

		String url = "https://m.sports.naver.com/";
		String sports;
		String params = "";
		String date = req.getParameter("month");
		try {
			sports = req.getParameter("sports");

			if (sports.equals("kbaseball")) {
				params = "category=kbo&date=";
			} else if (sports.equals("kfootball")) {
				params = "category=kleague&date=";
			} else if (sports.equals("basketball")) {
				params = "category=kbl&date=";
			} else if (sports.equals("volleyball")) {
				params = "category=kovo&date=";
			}

			url = url + sports + "/schedule/index?" + params +"2025-"+ date+"-01";
			Map<String, List<GameDTO>> map = sc.getSchduleMonth(url);

			mav.addObject("map", map);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/schedule/season", method = RequestMethod.GET)
	public ModelAndView season(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("schedule/season");
		
		return mav;
	}
	
	@RequestMapping(value = "/schedule/endseason", method = RequestMethod.GET)
	public ModelAndView endseason(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("schedule/endseason");
		
		
		return mav;
	}
}

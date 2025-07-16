package com.mopl.controller;

import java.io.IOException;
import java.util.List;

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
		
		String url = "https://m.sports.naver.com/";
		String sports;
		String params ="";
		String date = "2025-07-08";
		
		try {
			ScheduleCrawler sc = new ScheduleCrawler();
			
			sports = req.getParameter("sports");
			
			if (sports == null || sports.isEmpty()) {
			    sports = "kbaseball"; // 기본값 설정
			}
			
			if(sports.equals("kbaseball")) {
				params="date=";
			}else if(sports.equals("kfootball")) {
				params = "category=kleague&date=";
			}else if(sports.equals("basketball")) {
				params = "category=kbl&date=";
			}else if(sports.equals("volleyball")) {
				params= "category=kovo&date=";
			}
			
			url = url+sports+"/schedule/index?"+params+date;
			List<GameDTO> list = null;
			list = sc.getSchedule(url);
			
			mav.addObject("list", list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
}

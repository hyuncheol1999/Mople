package com.mopl.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

	@RequestMapping(value = "/schedule/matcharea", method = RequestMethod.POST)
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
			Map<String, List<GameDTO>> sortedMap = map.entrySet().stream()
				    .sorted(Comparator.comparingInt(e -> {
				        String key = e.getKey(); // 예: "7월 1일 (화)"
				        try {
				            // 숫자 추출: "7월 1일 (화)" → "1"
				            String dayStr = key.replaceAll("\\s", "")  // "7월1일(화)"
				                               .replaceAll(".*월", "")  // "1일(화)"
				                               .replaceAll("일.*", ""); // "1"
				            return Integer.parseInt(dayStr);
				        } catch (Exception ex) {
				            return Integer.MAX_VALUE;
				        }
				    }))
				    .collect(Collectors.toMap(
				        Map.Entry::getKey,
				        Map.Entry::getValue,
				        (e1, e2) -> e1,
				        LinkedHashMap::new // 순서 유지
				    ));

				mav.addObject("map", sortedMap);
			
			mav.addObject("map", sortedMap);

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

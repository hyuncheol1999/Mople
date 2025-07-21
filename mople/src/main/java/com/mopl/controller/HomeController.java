package com.mopl.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.dao.HomeDAO;
import com.mopl.model.MeetingDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.ResponseBody;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("home/main");

		try {
			HomeDAO dao = new HomeDAO();
			mav.addObject("popularMeetings", dao.getPopularMeetings(4));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping("/main/morePopularMeetings")
	@ResponseBody
	public Map<String, Object> morePopularMeetings(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> result = new HashMap<>();

		try {
			int offset = Integer.parseInt(req.getParameter("offset"));
			int limit = Integer.parseInt(req.getParameter("limit"));

			HomeDAO dao = new HomeDAO();
			List<MeetingDTO> list = dao.getPopularMeetings(offset, limit);

			result.put("success", true);
			result.put("popularMeetings", list);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
		}

		return result;
	}

}

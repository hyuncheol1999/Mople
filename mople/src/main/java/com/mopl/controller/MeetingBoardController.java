package com.mopl.controller;

import java.io.IOException;

import com.mopl.dao.MeetingBoardDAO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingBoardController {

	// 글 리스트
	@RequestMapping
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/meetingBoard/list");
		
		MeetingBoardDAO dao = new MeetingBoardDAO();

		return mav;
	}

}

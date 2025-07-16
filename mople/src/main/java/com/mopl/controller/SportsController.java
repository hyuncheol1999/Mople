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
public class SportsController {
	@RequestMapping(value = "/sports/facilities", method = RequestMethod.GET)
	public ModelAndView handleRestaurant(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("sports/facilities");
	}
}


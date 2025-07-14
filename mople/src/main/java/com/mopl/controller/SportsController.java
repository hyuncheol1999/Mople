package com.mopl.controller;

import java.io.IOException;

import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SportsController {
	@RequestMapping(value = "/sports/facilities", method = RequestMethod.GET)
	public String handleRestaurant(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return "sports/facilities";
	}	
}

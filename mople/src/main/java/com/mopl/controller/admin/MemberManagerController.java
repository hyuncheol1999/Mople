package com.mopl.controller.admin;

import java.io.IOException;
import java.util.List;

import com.mopl.dao.MemberDAO;
import com.mopl.model.MemberDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberManagerController {
	@RequestMapping(value = "/admin/member/list", method = RequestMethod.GET)
	public ModelAndView memberList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		
		ModelAndView mav = new ModelAndView("admin/member/list");
		
		try {
			List<MemberDTO> list;
			list = dao.memberList();
			
			mav.addObject("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
}

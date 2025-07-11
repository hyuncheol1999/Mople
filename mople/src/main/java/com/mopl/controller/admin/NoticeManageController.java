package com.mopl.controller.admin;

import java.io.IOException;
import java.util.List;

import com.mopl.dao.NoticeDAO;
import com.mopl.model.NoticeDTO;
import com.mopl.mvc.annotation.Controller;
import com.mopl.mvc.annotation.RequestMapping;
import com.mopl.mvc.annotation.RequestMethod;
import com.mopl.mvc.view.ModelAndView;
import com.mopl.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NoticeManageController {
	@RequestMapping(value = "/admin/notice/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		// 넘어온 파라미터 : [페이지번호, size, 검색컬럼, 검색값]
		ModelAndView mav = new ModelAndView("admin/notice/list");
		
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			
			// 한페이지 표시할 데이터 개수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);

			int dataCount, total_page;
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if (kwd.length() != 0) {
				list = dao.listNotice(offset, size, schType, kwd);
			} else {
				list = dao.listNotice(offset, size);
			}
			
			// 공지글
			
			// 공지글이 새로 등록된 것과의 시간 차
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// JSP로 포워딩
		return mav;	
	}
	
	

}

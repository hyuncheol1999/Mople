package com.mopl.map;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/map/*")
public class MapsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void viewPage(HttpServletRequest req, HttpServletResponse resp, String viewName)
			throws ServletException, IOException {
		final String REDIRECT_PREFIX = "redirect:";
		final String FORWARD_PREFIX = "/WEB-INF/views/";
		final String FORWARD_SUFFIX = ".jsp";

		// 리다이렉트
		if (viewName.startsWith(REDIRECT_PREFIX)) {
			String cp = req.getContextPath();
			String uri = cp + viewName.substring(REDIRECT_PREFIX.length());
			resp.sendRedirect(uri);
			return;
		}

		// 포워딩
		RequestDispatcher rd = req.getRequestDispatcher(FORWARD_PREFIX + viewName + FORWARD_SUFFIX);
		rd.forward(req, resp);
	}

	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// POST 방식 인코딩 설정
		req.setCharacterEncoding("utf-8");

		String method = req.getMethod(); // 메소드 : GET, POST 등
		String uri = req.getRequestURI();
		// cp 부터 끝까지 주소(QueryString은 제외)

		if (method.equals("GET")) {
			// GET 방식으로 요청한 경우
			if (uri.contains("/facilities")) {
				main(req, resp);
			} else if(uri.contains("/regions")) {
				regions(req, resp);
			} 
		} else if (method.equals("POST")) {
		}
	}
	
	protected void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		viewPage(req, resp, "sports/facilities");
	}
	
	protected void regions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String keyword = req.getParameter("keyword");
			
			List<Region> list = null;
			RegionService service = new RegionService();
			
			if(keyword == null) {
				list = service.listRegion();
			} else {
				list = service.listRegion(keyword);
			}
			
			JSONObject job = new JSONObject();
			job.put("list", list);
			
			resp.setContentType("application/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			
			out.print(job.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void facilities(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		viewPage(req, resp, "sports/facilities");
	}
	
}

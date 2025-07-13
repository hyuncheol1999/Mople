package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.SportCategoryDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class SportCategoryDAO {
	private Connection conn = DBConn.getConnection();
	
	// 스포츠 카테고리 리스트
	public List<SportCategoryDTO> findAllSportCategory() {
		List<SportCategoryDTO> list = new ArrayList<SportCategoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT sportIdx, sportName FROM sportCategory";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SportCategoryDTO dto = new SportCategoryDTO();
				dto.setSportIdx(rs.getInt("sportIdx"));
				dto.setSportName(rs.getString("sportName"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
}

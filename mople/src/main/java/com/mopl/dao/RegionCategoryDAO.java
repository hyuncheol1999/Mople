package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.RegionCategoryDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class RegionCategoryDAO {
	private Connection conn = DBConn.getConnection();
	
	// 지역 카테고리 리스트
	public List<RegionCategoryDTO> findAllRegionCategory() {
		List<RegionCategoryDTO> list = new ArrayList<RegionCategoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT regionIdx, regionName FROM regionCategory";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RegionCategoryDTO dto = new RegionCategoryDTO();
				dto.setRegionIdx(rs.getInt("regionIdx"));
				dto.setRegionName(rs.getString("regionName"));
				
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

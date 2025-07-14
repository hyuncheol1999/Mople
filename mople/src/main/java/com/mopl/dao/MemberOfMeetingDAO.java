package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MemberOfMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MemberOfMeetingDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<MemberOfMeetingDTO> findMeetingIdx(long meetingIdx) {
		List<MemberOfMeetingDTO> list = new ArrayList<MemberOfMeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT meetingIdx, mom.memberIdx, mom.role, userName, profilePhoto ");
			sb.append("FROM memberOfMeeting mom ");
			sb.append("LEFT OUTER JOIN member1 m1 ON mom.memberIdx = m1.memberIdx ");
			sb.append("LEFT OUTER JOIN member2 m2 ON mom.memberIdx = m2.memberIdx ");
			sb.append("WHERE meetingIdx = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, meetingIdx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				MemberOfMeetingDTO dto = new MemberOfMeetingDTO();
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setRole(rs.getInt("role"));
				dto.setMemberName(rs.getString("userName"));
				dto.setMemberProfilePhoto(rs.getString("profilePhoto"));
				
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
	
	public boolean isLeader(long meetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT role FROM MemberOfMeeting WHERE meetingIdx=? AND memberIdx=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, meetingIdx);
			pstmt.setLong(2, memberIdx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("role") == 0;
			}
			return false;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
}


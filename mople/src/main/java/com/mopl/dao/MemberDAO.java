package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mopl.model.MemberDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO loginMember(String userId, String userPwd) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m1.memberIdx, m1.userId, userPwd, userName, userNickName, birth, address, gender, reg_date, role, email, tel, profilePhoto"
					+ " FROM member1 m1 LEFT OUTER JOIN member2 m2 ON m1.memberIdx = m2.memberIdx "
					+ " WHERE m1.userId = ? AND userPwd = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddress(rs.getString("address"));
				dto.setGender(rs.getInt("gender"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				dto.setProfilePhoto(rs.getString("profilePhoto"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		
		return dto;
	}
	
	public void insertMember(MemberDTO dto) throws SQLException {

	}
	
	public MemberDTO findById(String userId) {
		
		return null;
	}
	
	public void updateMember(MemberDTO dto) throws SQLException {

	}
	
	public void deleteProfilePhoto(String userId) throws SQLException {
		
	}

}

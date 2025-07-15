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
	
	// 모임 전체 인원 조회
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
	
	// 모임장인지 확인
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
	
	// 로그인한 회원이 모임에 가입된 멤버인지 확인
	public boolean isMeetingMember(long meetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM memberOfMeeting WHERE meetingIdx = ? AND memberIdx = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, meetingIdx);
			pstmt.setLong(2, memberIdx);
			
			rs = pstmt.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
	
	// 모임원 등록 - 대기
	public void insertMeetingMember(MemberOfMeetingDTO dto) {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("INSERT INTO memberOfMeeting(meetingIdx, memberIdx, role)");
			sb.append("  VALUES(?, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, dto.getMeetingIdx());
			pstmt.setLong(2, dto.getMemberIdx());
			pstmt.setInt(3, dto.getRole());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 모임원 등록 - 승인
	public void approveMember(MemberOfMeetingDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE memberOfMeeting SET role = ? WHERE meetingIdx = ? AND memberIdx = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getRole());
			pstmt.setLong(2, dto.getMeetingIdx());
			pstmt.setLong(3, dto.getMemberIdx());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 모임원 등록 - 거절
	public void rejectMember(MemberOfMeetingDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM memberOfMeeting WHERE meetingIdx = ? AND memberIdx = ?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getMeetingIdx());
			pstmt.setLong(2, dto.getMemberIdx());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
}


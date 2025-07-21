package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MemberOfBungaeMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MemberOfBungaeMeetingDAO  {
	
	private Connection conn = DBConn.getConnection();
	
	// 번개모임 전체 인원 조회 (승인된 인원)
	public List<MemberOfBungaeMeetingDTO> findByBungaeMeetingIdx(long bungaeMeetingIdx) {
		List<MemberOfBungaeMeetingDTO> list = new ArrayList<MemberOfBungaeMeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT bungaeMeetingIdx, mom.memberIdx, mom.role, userName, userNickName, profilePhoto ");
			sb.append("FROM memberOfBungaeMeeting mbm ");
			sb.append("LEFT OUTER JOIN member1 m1 ON mbm.memberIdx = m1.memberIdx ");
			sb.append("LEFT OUTER JOIN member2 m2 ON mbm.memberIdx = m2.memberIdx ");
			sb.append("WHERE bungaeMeetingIdx = ? AND mbm.role != 2 ");
			sb.append("ORDER BY mbm.role");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, bungaeMeetingIdx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setRole(rs.getInt("role"));
				dto.setMemberName(rs.getString("userName"));
				dto.setMemberNickName(rs.getString("userNickName"));
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
	
	// 승인 대기 인원 조회
	public List<MemberOfBungaeMeetingDTO> findWaitingList(long bungaeMeetingIdx) {
		List<MemberOfBungaeMeetingDTO> list = new ArrayList<MemberOfBungaeMeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT mbm.bungaeMeetingIdx, mbm.memberIdx, mbm.role, userName, profilePhoto ");
			sb.append("FROM memberOfBungaeMeeting mbm ");
			sb.append("LEFT OUTER JOIN member1 m1 ON mbm.memberIdx = m1.memberIdx ");
			sb.append("LEFT OUTER JOIN member2 m2 ON mbm.memberIdx = m2.memberIdx ");
			sb.append("WHERE mbm.bungaeMeetingIdx = ? AND mbm.role = 2");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, bungaeMeetingIdx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
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
	
	// 번개모임 전체 인원 카운트 (대기 인원 제외 (role=2))
	public int findMemberCount(long bungaeMeetingIdx) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		int waitingCount = 0;
		
		try {
			sb.append("SELECT COUNT(*) count ");
			sb.append("FROM memberOfBungaeMeeting mbm ");
			sb.append("LEFT OUTER JOIN member1 m1 ON mbm.memberIdx = m1.memberIdx ");
			sb.append("LEFT OUTER JOIN member2 m2 ON mbm.memberIdx = m2.memberIdx ");
			sb.append("WHERE mbm.bungaeMeetingIdx = ? AND mbm.role != 2");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, bungaeMeetingIdx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				waitingCount = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return waitingCount;
	}
	
	
	
	// 번개모임장인지 확인
	public boolean isLeader(long bungaeMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT role FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx=? AND memberIdx=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bungaeMeetingIdx);
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
	
	// 승인 대기인원인지 확인
	public boolean isWaiting(long bungaeMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT role FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx=? AND memberIdx=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bungaeMeetingIdx);
			pstmt.setLong(2, memberIdx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("role") == 2;
			}
			return false;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
	/*
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
	*/
	// 번개모임원 등록 - 대기
	public void insertaBungaeMeetingMember(MemberOfBungaeMeetingDTO dto) {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("INSERT INTO memberOfBungaeMeeting(bungaeMeetingIdx, memberIdx, role)");
			sb.append("  VALUES(?, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, dto.getBungaeMeetingIdx());
			pstmt.setLong(2, dto.getMemberIdx());
			pstmt.setInt(3, dto.getRole());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 번개모임원 역할 수정 / 번개모임원 등록 - 승인
	public void updateMemberRole(MemberOfBungaeMeetingDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE memberOfBungaeMeeting SET role = ? WHERE bungaeMeetingIdx = ? AND memberIdx = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getRole());
			pstmt.setLong(2, dto.getBungaeMeetingIdx());
			pstmt.setLong(3, dto.getMemberIdx());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 번개모임 탈퇴 / 번개모임원 등록 - 거절
	public void leaveMeeting(MemberOfBungaeMeetingDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx = ? AND memberIdx = ?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getBungaeMeetingIdx());
			pstmt.setLong(2, dto.getMemberIdx());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 번개모임에 이미 등록된 멤버인지 확인
	public boolean exists(long bungaeMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx = ? AND memberIdx = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bungaeMeetingIdx);
			pstmt.setLong(2, memberIdx);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return false;
	}
}



package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MeetingDTO;
import com.mopl.model.MemberDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MeetingDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertMeeting(MeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			conn.setAutoCommit(false);
			
			sb.append("INSERT INTO meeting(meetingIdx, meetingName, meetingDesc, createdDate, meetingProfilePhoto, regionIdx, sportIdx) ");
			sb.append("VALUES(meeting_seq.NEXTVAL, ?, ?, SYSDATE, ?, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getMeetingName());
			pstmt.setString(2, dto.getMeetingDesc());
			pstmt.setString(3, dto.getMeetingProfilePhoto());
			pstmt.setInt(4, dto.getRegionIdx());
			pstmt.setInt(5, dto.getSportIdx());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			sb = new StringBuilder();
			
			sb.append("INSERT INTO memberOfMeeting(meetingIdx, memberIdx, role) ");
			sb.append("VALUES(meeting_seq.CURRVAL, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, dto.getMemberIdx());
			pstmt.setInt(2, dto.getRole());
			
			pstmt.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);				
			} catch (Exception e2) {
			}
		}
	}
	
	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT COUNT(*) FROM meeting";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 검색에서의 데이터 개수
	// 검색 조건 : 스포츠 카테고리, 지역별 카테고리
	public int dataCount(int sportCategory, int regionCategory) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT COUNT(*) FROM meeting ");
			 
			pstmt = conn.prepareStatement(sb.toString());
			
			if(sportCategory != 0 && regionCategory != 0) {
				sb.append("WHERE sportCategory = ? AND regionCategory = ?");	
			} else if(sportCategory == 0) {
				sb.append("WHERE regionCategory = ?");
			} else if(regionCategory == 0) {
				sb.append("WHERE sportCategory = ?");				
			}
			
			if(sportCategory != 0 && regionCategory != 0) {
				pstmt.setInt(1, sportCategory);
				pstmt.setInt(2, regionCategory);
			} else if(sportCategory == 0) {
				pstmt.setInt(1, regionCategory);
				
			} else if(regionCategory == 0) {
				pstmt.setInt(1, sportCategory);			
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	// 유저가 참여한 미팅 리스트
	public List<MeetingDTO> findByUserIdx(long memberIdx) {
		List<MeetingDTO> list = new ArrayList<MeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.meetingIdx, meetingName, meetingDesc, createdDate, meetingProfilePhoto,");
			sb.append("  regionName, sportName");			
			sb.append("FROM meeting m1");
			sb.append("LEFT OUTER JOIN memberOfMeeting m2 ON m1.meetingIdx = m2.meetingIdx");
			sb.append("LEFT OUTER JOIN sportCategory sc ON m1.sportIdx = sc.sportIdx");
			sb.append("LEFT OUTER JOIN regionCategory rc ON m1.regionIdx = rc.regionIdx");
			sb.append("WHERE memberIdx = ?");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, memberIdx);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MeetingDTO dto = new MeetingDTO();
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));
				
				// 작성중
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}

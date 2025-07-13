package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MeetingDTO;
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
	public int dataCount(int sportIdx, int regionIdx) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT COUNT(*) FROM meeting ");
			 
			if(sportIdx != 0 && regionIdx != 0) {
				sb.append("WHERE sportIdx = ? AND regionIdx = ?");	
			} else if(sportIdx == 0) {
				sb.append("WHERE regionIdx = ?");
			} else if(regionIdx == 0) {
				sb.append("WHERE sportIdx = ?");				
			}
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(sportIdx != 0 && regionIdx != 0) {
				pstmt.setInt(1, sportIdx);
				pstmt.setInt(2, regionIdx);
			} else if(sportIdx == 0) {
				pstmt.setInt(1, regionIdx);
				
			} else if(regionIdx == 0) {
				pstmt.setInt(1, sportIdx);			
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
	
	// 전체 모임 리스트
	public List<MeetingDTO> findAllMeetings(int offset, int size, int sportCategory, int regionCategory, String sortBy) {
		List<MeetingDTO> list = new ArrayList<MeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.meetingIdx, m1.meetingName, m1.meetingDesc, TO_CHAR(m1.createdDate, 'YYYY-MM-DD') createdDate,");
			sb.append("  m1.meetingProfilePhoto, regionName, sportName, COUNT(m2.memberIdx) currentMembers ");			
			sb.append("FROM meeting m1 ");
			sb.append("LEFT OUTER JOIN memberOfMeeting m2 ON m1.meetingIdx = m2.meetingIdx ");
			sb.append("LEFT OUTER JOIN sportCategory sc ON m1.sportIdx = sc.sportIdx ");
			sb.append("LEFT OUTER JOIN regionCategory rc ON m1.regionIdx = rc.regionIdx ");
			if(sportCategory != 0 && regionCategory != 0) {
				sb.append("WHERE m1.sportIdx = ? AND m1.regionIdx = ? ");	
			} else if(sportCategory == 0 && regionCategory == 0) {
				// 전체 선택 시 조건 X
			} else if(sportCategory == 0) {
				sb.append("WHERE m1.regionIdx = ? ");
			} else if(regionCategory == 0) {
				sb.append("WHERE m1.sportIdx = ? ");				
			}
			
			sb.append("GROUP BY m1.meetingIdx, m1.meetingName, m1.meetingDesc, m1.createdDate,");
	        sb.append("  m1.meetingProfilePhoto, rc.regionName, sc.sportName ");
	        
	        // 정렬 기준
	        if(sortBy == null) {
	        	sb.append("ORDER BY m1.createdDate DESC ");
	        } else if(sortBy.equals("popular")) {
	        	// 인원수 기준
	        	sb.append("ORDER BY currentMembers DESC "); 
	        } else {
	        	sb.append("ORDER BY m1.createdDate DESC ");	        	
	        }
	        
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(sportCategory != 0 && regionCategory != 0) {
				pstmt.setInt(1, sportCategory);
				pstmt.setInt(2, regionCategory);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else if(sportCategory == 0 && regionCategory == 0) {
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);
			} else if(sportCategory == 0) {
				pstmt.setInt(1, regionCategory);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);				
			} else if(regionCategory == 0) {
				pstmt.setInt(1, sportCategory);			
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MeetingDTO dto = new MeetingDTO();
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMeetingName(rs.getString("meetingName"));
				dto.setMeetingDesc(rs.getString("meetingDesc"));
				dto.setCreatedDate(rs.getString("createdDate"));
				dto.setMeetingProfilePhoto(rs.getString("meetingProfilePhoto"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));
				
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
	
	
	// 유저가 참여한 모임 리스트
	public List<MeetingDTO> findByMemberIdx(long memberIdx) {
		List<MeetingDTO> list = new ArrayList<MeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.meetingIdx, meetingName, meetingDesc, createdDate, meetingProfilePhoto,");
			sb.append("  regionName, sportName, COUNT(m2.memberIdx) currentMembers ");			
			sb.append("FROM meeting m1 ");
			sb.append("LEFT OUTER JOIN memberOfMeeting m2 ON m1.meetingIdx = m2.meetingIdx ");
			sb.append("LEFT OUTER JOIN sportCategory sc ON m1.sportIdx = sc.sportIdx ");
			sb.append("LEFT OUTER JOIN regionCategory rc ON m1.regionIdx = rc.regionIdx ");
			sb.append("WHERE memberIdx = ?");
			sb.append("GROUP BY m1.meetingIdx, m1.meetingName, m1.meetingDesc, m1.createdDate,");
	        sb.append("  m1.meetingProfilePhoto, rc.regionName, sc.sportName ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, memberIdx);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MeetingDTO dto = new MeetingDTO();

				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMeetingName(rs.getString("meetingName"));
				dto.setMeetingDesc(rs.getString("meetingDesc"));
				dto.setCreatedDate(rs.getString("createdDate"));
				dto.setMeetingProfilePhoto(rs.getString("meetingProfilePhoto"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	// 모임 정보
	public MeetingDTO findByMeeetingIdx(long memberIdx) {
		MeetingDTO dto = new MeetingDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.meetingIdx, meetingName, meetingDesc, createdDate, meetingProfilePhoto,");
			sb.append("  regionName, sportName, COUNT(m2.memberIdx) currentMembers ");			
			sb.append("FROM meeting m1 ");
			sb.append("LEFT OUTER JOIN memberOfMeeting m2 ON m1.meetingIdx = m2.meetingIdx ");
			sb.append("LEFT OUTER JOIN sportCategory sc ON m1.sportIdx = sc.sportIdx ");
			sb.append("LEFT OUTER JOIN regionCategory rc ON m1.regionIdx = rc.regionIdx ");
			sb.append("WHERE m1.meetingIdx = ?");
			sb.append("GROUP BY m1.meetingIdx, m1.meetingName, m1.meetingDesc, m1.createdDate,");
	        sb.append("  m1.meetingProfilePhoto, rc.regionName, sc.sportName ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, memberIdx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMeetingName(rs.getString("meetingName"));
				dto.setMeetingDesc(rs.getString("meetingDesc"));
				dto.setCreatedDate(rs.getString("createdDate"));
				dto.setMeetingProfilePhoto(rs.getString("meetingProfilePhoto"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
}

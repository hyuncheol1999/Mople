package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.RegularMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class RegularMeetingDAO {
	private Connection conn = DBConn.getConnection();

	// 어떤 모임의 정모일정리스트 + 현재 참여인원
	public List<RegularMeetingDTO> listSchedule(long meetingIdx) throws SQLException {
		List<RegularMeetingDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.regularMeetingIdx, meetingIdx, startDate, place, capacity, subject, status, "
					+ " NVL( COUNT(m.memberIdx), 0 ) AS currentCnt " + " FROM regularMeeting r "
					+ " LEFT JOIN memberOfRegularMeeting m ON m.regularMeetingIdx = r.regularMeetingIdx "
					+ " WHERE meetingIdx = ? AND isBungaeMeeting = 0 "
					+ " GROUP BY r.regularMeetingIdx, meetingIdx, startDate, place, capacity, subject, status "
					+ " ORDER BY startDate ASC";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, meetingIdx);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RegularMeetingDTO dto = new RegularMeetingDTO();
				dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
				dto.setStartDate(rs.getString("startDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setStatus(rs.getInt("status"));
				dto.setCurrentCnt(rs.getInt("currentCnt"));

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

	// 해당 회원이 정모에 이미 참여했는지 중복체크
	public boolean isJoined(long regularMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM memberOfRegularMeeting WHERE regularMeetingIdx=? AND memberIdx=?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, regularMeetingIdx);
			pstmt.setLong(2, memberIdx);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return false;
	}

	// 정모 참여 ( 중복체크 )
	public void insertParticipant(long regularMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "SELECT COUNT(*) FROM memberOfRegularMeeting " + " WHERE regularMeetingIdx = ? AND memberIdx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, regularMeetingIdx);
			pstmt.setLong(2, memberIdx);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				System.out.println("이미 참여했습니다.");
				return;
			}

			pstmt.close();
			pstmt = null;

			sql = "INSERT INTO memberOfRegularMeeting (regularMeetingIdx, memberIdx) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, regularMeetingIdx);
			pstmt.setLong(2, memberIdx);
			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			conn.setAutoCommit(true);
		}
	}

	// 정모 참여취소
	public void deleteParticipant(long regularMeetingIdx, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM memberOfRegularMeeting WHERE regularMeetingIdx = ? AND memberIdx = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, regularMeetingIdx);
			pstmt.setLong(2, memberIdx);
			pstmt.executeUpdate();

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

// 정모장이 할것	
	// 정모 생성
	public void createRegularMeeting(RegularMeetingDTO dto) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	    	conn.setAutoCommit(false);
	        sql = "INSERT INTO regularMeeting(regularMeetingIdx, startDate, place, capacity, subject, sportIdx, regionIdx, meetingIdx, memberIdx) "
	            + "VALUES(regularMeeting_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getStartDate());
	        pstmt.setString(2, dto.getPlace());
	        pstmt.setInt(3, dto.getCapacity());
	        pstmt.setString(4, dto.getSubject());
	        pstmt.setInt(5, dto.getSportIdx());
	        pstmt.setInt(6, dto.getRegionIdx());
	        pstmt.setLong(7, dto.getMeetingIdx());
	        pstmt.setLong(8, dto.getMemberIdx());
	        pstmt.executeUpdate();

	        conn.commit();
	    } catch (Exception e) {
	        conn.rollback();
	        throw e;
	    } finally {
	        DBUtil.close(pstmt);
	        conn.setAutoCommit(true);
	    }
	}
/*  정모장도 자동참여됨
	public void createRegularMeeting(RegularMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO regularMeeting(regularMeetingIdx, startDate, place, capacity, subject, sportIdx, regionIdx, meetingIdx, memberIdx) "
					+ "VALUES(regularMeeting_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getStartDate());
			pstmt.setString(2, dto.getPlace());
			pstmt.setInt(3, dto.getCapacity());
			pstmt.setString(4, dto.getSubject());
			pstmt.setInt(5, dto.getSportIdx());
			pstmt.setInt(6, dto.getRegionIdx());
			pstmt.setLong(7, dto.getMeetingIdx());
			pstmt.setLong(8, dto.getMemberIdx());
			pstmt.executeUpdate();

			pstmt.close();

			sql = "INSERT INTO memberOfRegularMeeting(regularMeetingIdx, memberIdx) VALUES(regularMeeting_seq.CURRVAL, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getMemberIdx());
			
			pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
		DBUtil.close(pstmt);
		conn.setAutoCommit(true);
		}
	}
*/
	// 정모 수정
	public void updateRegularMeeting(RegularMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE regularMeeting SET subject=?, startDate=TO_DATE(?, 'YYYY-MM-DD'), place=?, capacity=? "
					+ " WHERE meetingIdx=? AND regularMeetingIdx=? AND isBungaeMeeting=0";

			pstmt = conn.prepareStatement(sql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getStartDate());
			pstmt.setString(3, dto.getPlace());
			pstmt.setInt(4, dto.getCapacity());
			pstmt.setLong(5, dto.getMeetingIdx());
			pstmt.setLong(6, dto.getRegularMeetingIdx());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 정모 삭제
	public int deleteRegularMeeting(long regularMeetingIdx) throws SQLException {
	    PreparedStatement pstmt = null;
	    
	    try {
	    	String sql = "DELETE FROM regularMeeting WHERE regularMeetingIdx = ?";
	        
	    	pstmt = conn.prepareStatement(sql);
	    	
	    	pstmt.setLong(1, regularMeetingIdx);
	        
	    	return pstmt.executeUpdate();
	    } finally {
			DBUtil.close(pstmt);
		}
	}

	
	public RegularMeetingDTO findByRegularMeetingIdx(long regularMeetingIdx) throws SQLException {
	    RegularMeetingDTO dto = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT regularMeetingIdx, startDate, place, capacity, subject, sportIdx, regionIdx, meetingIdx, memberIdx "
	            + "FROM regularMeeting WHERE regularMeetingIdx = ?";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, regularMeetingIdx);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            dto = new RegularMeetingDTO();
	            dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
	            dto.setStartDate(rs.getDate("startDate").toString());
	            dto.setPlace(rs.getString("place"));
	            dto.setCapacity(rs.getInt("capacity"));
	            dto.setSubject(rs.getString("subject"));
	            dto.setSportIdx(rs.getInt("sportIdx"));
	            dto.setRegionIdx(rs.getInt("regionIdx"));
	            dto.setMeetingIdx(rs.getLong("meetingIdx"));
	            dto.setMemberIdx(rs.getLong("memberIdx"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return dto;
	}


}
package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.RegularMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class RegularMeetingDAO {
	private Connection conn = DBConn.getConnection();
	
	private LocalDateTime ts(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t != null ? t.toLocalDateTime() : null;
    }

	// 어떤 모임의 정모일정리스트 + 현재 참여인원
	public List<RegularMeetingDTO> listSchedule(long meetingIdx) throws SQLException {
		List<RegularMeetingDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.regularMeetingIdx, meetingIdx, startDate, endDate, place, capacity, subject, status, "
					+ " NVL( COUNT(m.memberIdx), 0 ) AS currentCnt " + " FROM regularMeeting r "
					+ " LEFT JOIN memberOfRegularMeeting m ON m.regularMeetingIdx = r.regularMeetingIdx "
					+ " WHERE meetingIdx = ? AND isBungaeMeeting = 0 "
					+ " GROUP BY r.regularMeetingIdx, meetingIdx, startDate, endDate, place, capacity, subject, status "
					+ " ORDER BY startDate ASC";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, meetingIdx);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RegularMeetingDTO dto = new RegularMeetingDTO();
				dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
				dto.setStartDate(ts(rs, "startDate"));
                dto.setEndDate(ts(rs, "endDate"));
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
	    	sql = "INSERT INTO regularMeeting(regularMeetingIdx, startDate, endDate, place, capacity, subject, sportIdx, regionIdx, meetingIdx, memberIdx, status, isBungaeMeeting) "
	                + "VALUES(regularMeeting_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0)";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setTimestamp(1, Timestamp.valueOf(dto.getStartDate()));
	        pstmt.setTimestamp(2, Timestamp.valueOf(dto.getEndDate()));
	        pstmt.setString(3, dto.getPlace());
	        pstmt.setInt(4, dto.getCapacity());
	        pstmt.setString(5, dto.getSubject());
	        pstmt.setInt(6, dto.getSportIdx());
	        pstmt.setInt(7, dto.getRegionIdx());
	        pstmt.setLong(8, dto.getMeetingIdx());
	        pstmt.setLong(9, dto.getMemberIdx());
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

	// 정모 수정
	public void updateRegularMeeting(RegularMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE regularMeeting SET subject=?, startDate=?, endDate=?, place=?, capacity=? "
				    + "WHERE regularMeetingIdx=? AND meetingIdx=? AND isBungaeMeeting=0";

			pstmt = conn.prepareStatement(sql);

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setTimestamp(2, Timestamp.valueOf(dto.getStartDate()));
			pstmt.setTimestamp(3, Timestamp.valueOf(dto.getEndDate()));
			pstmt.setString(4, dto.getPlace());
			pstmt.setInt(5, dto.getCapacity());
			pstmt.setLong(6, dto.getRegularMeetingIdx()); 
			pstmt.setLong(7, dto.getMeetingIdx());
			
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

	// 정모 하나 조회
	public RegularMeetingDTO findByRegularMeetingIdx(long regularMeetingIdx) throws SQLException {
	    RegularMeetingDTO dto = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT regularMeetingIdx, startDate, endDate, place, capacity, subject, sportIdx, regionIdx, meetingIdx, memberIdx, status, isBungaeMeeting "
	        	    + "FROM regularMeeting WHERE regularMeetingIdx = ?";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, regularMeetingIdx);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	        	dto = new RegularMeetingDTO();
                dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
                dto.setMeetingIdx(rs.getLong("meetingIdx"));
                dto.setStartDate(ts(rs, "startDate"));
                dto.setEndDate(ts(rs, "endDate"));
                dto.setPlace(rs.getString("place"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setSubject(rs.getString("subject"));
                dto.setSportIdx(rs.getInt("sportIdx"));
                dto.setRegionIdx(rs.getInt("regionIdx"));
                dto.setMemberIdx(rs.getLong("memberIdx"));
                dto.setStatus(0);
                dto.setIsBungaeMeeting(0);
	        } 
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return dto;
	}
	
	// 정기모임 -> 번개모임으로 전환 
		public void changeBungae(long regularMeetingIdx) throws SQLException {
		    String sql = "UPDATE regularMeeting SET isBungaeMeeting = 1 WHERE regularMeetingIdx = ?";
		    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setLong(1, regularMeetingIdx);
		        pstmt.executeUpdate();
		    }
		}
		
	// 정기모임 중 번개로 전환된 모임 조회 (isBungaeMeeting = 1)
		public List<RegularMeetingDTO> selectUrgentRegularMeetings(String keyword) throws Exception {
		    List<RegularMeetingDTO> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql;

		    try {
		        sql = "SELECT meetingIdx, subject, content, startDate, endDate, place, capacity, isBungaeMeeting "
		            + "FROM RegularMeeting "
		            + "WHERE isBungaeMeeting = 1 AND (subject LIKE ? OR content LIKE ?) "
		            + "ORDER BY startDate ASC";

		        pstmt = conn.prepareStatement(sql);
		        String kw = "%" + keyword + "%";
		        pstmt.setString(1, kw);
		        pstmt.setString(2, kw);

		        rs = pstmt.executeQuery();

		        while (rs.next()) {
		            RegularMeetingDTO dto = new RegularMeetingDTO();
		            dto.setMeetingIdx(rs.getLong("meetingIdx"));
		            dto.setSubject(rs.getString("subject"));
		            dto.setContent(rs.getString("content"));
		            dto.setStartDate(ts(rs, "startDate"));
                    dto.setEndDate(ts(rs, "endDate"));
		            dto.setPlace(rs.getString("place"));
		            dto.setCapacity(rs.getInt("capacity"));
		            dto.setIsBungaeMeeting(1);

		            list.add(dto);
		        }

		    } finally {
		        DBUtil.close(rs);
		        DBUtil.close(pstmt);
		    }

		    return list;
		}

		// 번개모임에 정모 띄울거 
		public List<RegularMeetingDTO> getOngoingMeetingsTop5() {
		    List<RegularMeetingDTO> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql;

		    try {
		        sql = "SELECT regularMeetingIdx, subject, startDate, endDate, place, capacity, status, isBungaeMeeting "
		            + "FROM regularMeeting "
		            + "WHERE status = 0 "
		            + "ORDER BY startDate ASC "
		            + "FETCH FIRST 5 ROWS ONLY";  

		        pstmt = conn.prepareStatement(sql);
		        rs = pstmt.executeQuery();

		        while (rs.next()) {
		            RegularMeetingDTO dto = new RegularMeetingDTO();

		            dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
		            dto.setSubject(rs.getString("subject"));
		            dto.setStartDate(ts(rs, "startDate"));
	                dto.setEndDate(ts(rs, "endDate"));
		            dto.setPlace(rs.getString("place"));
		            dto.setCapacity(rs.getInt("capacity"));
		            dto.setStatus(0);
		            dto.setIsBungaeMeeting(1);

		            list.add(dto);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        DBUtil.close(rs);
		        DBUtil.close(pstmt);
		    }

		    return list;
		}
		
		// 참여하고 있는 정모 리스트
		public List<RegularMeetingDTO> findByMemberIdx(long memberIdx) throws SQLException {
			List<RegularMeetingDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT r.regularMeetingIdx, meetingIdx, startDate, endDate, place, capacity, subject, status, "
						+ " NVL( COUNT(m.memberIdx), 0 ) AS currentCnt " + " FROM regularMeeting r "
						+ " LEFT JOIN memberOfRegularMeeting m ON m.regularMeetingIdx = r.regularMeetingIdx "
						+ " WHERE r.memberIdx = ? AND endDate >= SYSDATE "
						+ " GROUP BY r.regularMeetingIdx, meetingIdx, startDate, endDate, place, capacity, subject, status "
						+ " ORDER BY startDate ASC";

				pstmt = conn.prepareStatement(sql);

				pstmt.setLong(1, memberIdx);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					RegularMeetingDTO dto = new RegularMeetingDTO();
					dto.setRegularMeetingIdx(rs.getLong("regularMeetingIdx"));
	                dto.setMeetingIdx(rs.getLong("meetingIdx"));
	                dto.setStartDate(ts(rs, "startDate"));
	                dto.setEndDate(ts(rs, "endDate"));
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

}
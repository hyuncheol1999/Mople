package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.BungaeMeetingDTO;
import com.mopl.model.MemberOfBungaeMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class BungaeMeetingDAO {
	private Connection conn = DBConn.getConnection();
	
	private LocalDateTime ts(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t != null ? t.toLocalDateTime() : null;
    }
	
	// 번개모임 생성
	public void insertBungaeMeeting(BungaeMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			conn.setAutoCommit(false);

			sb.append( "INSERT INTO bungaeMeeting(bungaeMeetingIdx, subject, content, startDate, endDate, place, capacity, status, categoryIdx, regionIdx, bungaeMemberIdx) ");
			sb.append( " VALUES (bungaeMeeting_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?)");		        

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setTimestamp(3, Timestamp.valueOf(dto.getStartDate()));
	        pstmt.setTimestamp(4, Timestamp.valueOf(dto.getEndDate()));
			pstmt.setString(5, dto.getPlace());
			pstmt.setInt(6, dto.getCapacity());
			pstmt.setInt(7, dto.getCategoryIdx());
			pstmt.setInt(8, dto.getRegionIdx());
			pstmt.setLong(9, dto.getBungaeMemberIdx());

			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;
			sb = new StringBuilder();

			sb.append("INSERT INTO memberOfBungaeMeeting(bungaeMeetingIdx, memberIdx, role)" );
			sb.append(" VALUES (bungaeMeeting_seq.CURRVAL, ?, 0)");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, dto.getBungaeMemberIdx());
			
			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
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
			sql = "SELECT COUNT(*) FROM bungaeMeeting";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
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
			sb.append("SELECT COUNT(*) FROM bungaeMeeting ");

			if (sportIdx != 0 && regionIdx != 0) {
				sb.append("WHERE sportIdx = ? AND regionIdx = ?");
			} else if (sportIdx == 0) {
				sb.append("WHERE regionIdx = ?");
			} else if (regionIdx == 0) {
				sb.append("WHERE sportIdx = ?");
			}

			pstmt = conn.prepareStatement(sb.toString());

			if (sportIdx != 0 && regionIdx != 0) {
				pstmt.setInt(1, sportIdx);
				pstmt.setInt(2, regionIdx);
			} else if (sportIdx == 0) {
				pstmt.setInt(1, regionIdx);

			} else if (regionIdx == 0) {
				pstmt.setInt(1, sportIdx);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 전체 모임 리스트
	public List<BungaeMeetingDTO> findAllBungaeMeetings(int offset, int size, int sportCategory, int regionCategory,
			String sortBy) {
		List<BungaeMeetingDTO> list = new ArrayList<BungaeMeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(
					"SELECT bm.bungaeMeetingIdx, bm.startDate, bm.endDate, bm.place, bm.capacity, bm.subject, bm.content, status, sportName, regionName, bm.bungaeMemberIdx, COUNT(mb.bungaeMemberIdx) currentCnt");
			sb.append(" FROM bungaeMeeting bm ");
			sb.append(" LEFT OUTER JOIN memberOfBungaeMeeting mb ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
			sb.append(" LEFT OUTER JOIN member1 m1 ON bm.bungaeMemberIdx = m1.memberIdx ");
			sb.append(" LEFT OUTER JOIN sportCategory s ON bm.categoryIdx = s.sportIdx ");
			sb.append(" LEFT OUTER JOIN regionCategory r ON bm.regionIdx = r.regionIdx ");

			if (sportCategory != 0 && regionCategory != 0) {
				sb.append("WHERE bm.sportIdx = ? AND bm.regionIdx = ? ");
			} else if (sportCategory == 0 && regionCategory == 0) {
			} else if (sportCategory == 0) {
				sb.append("WHERE bm.regionIdx = ? ");
			} else if (regionCategory == 0) {
				sb.append("WHERE bm.sportIdx = ? ");
			}

			sb.append(
					"GROUP BY bm.bungaeMeetingIdx, bm.startDate, bm.endDate, bm.place, bm.capacity, bm.subject, bm.content, status, s.sportIdx, r.regionIdx, bm.bungaeMemberIdx ");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			// 정렬기준 생략 했음

			pstmt = conn.prepareStatement(sb.toString());

			if (sportCategory != 0 && regionCategory != 0) {
				pstmt.setInt(1, sportCategory);
				pstmt.setInt(2, regionCategory);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else if (sportCategory == 0 && regionCategory == 0) {
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);
			} else if (sportCategory == 0) {
				pstmt.setInt(1, regionCategory);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			} else if (regionCategory == 0) {
				pstmt.setInt(1, sportCategory);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BungaeMeetingDTO dto = new BungaeMeetingDTO();
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setStartDate(ts(rs, "startDate"));
                dto.setEndDate(ts(rs, "endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentCnt(rs.getInt("currentCnt"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;

	}

	// 유저가 참여한 번개모임 리스트
	public List<BungaeMeetingDTO> findByBungaeMemberIdx(long bungaeMemberIdx) {
		List<BungaeMeetingDTO> list = new ArrayList<BungaeMeetingDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(
					"SELECT bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, sportName, regionName, bungaeMemberIdx, COUNT(mb.bungaeMemberIdx) currentCnt ");
			sb.append(" FROM bungaeMeeting bm ");
			sb.append(" LEFT OUTER JOIN memberOfBungaeMeeting mb ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
			sb.append(" LEFT OUTER JOIN sportCategory s ON mb.categoryIdx = s.sportIdx ");
			sb.append(" LEFT OUTER JOIN regionCategory r ON mb.regionIdx = r.regionIdx ");
			sb.append(" WHERE bungaeMemberIdx = ?");
			sb.append(
					" GROUP BY bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, s.sportIdx, r.regionIdx, bungaeMemberIdx ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, bungaeMemberIdx);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BungaeMeetingDTO dto = new BungaeMeetingDTO();

				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setStartDate(ts(rs, "startDate"));
                dto.setEndDate(ts(rs, "endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentCnt(rs.getInt("currentCnt"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 번개모임 정보
	public BungaeMeetingDTO findByBungaeMeetingIdx(long bungaeMeetingIdx) {
		BungaeMeetingDTO dto = new BungaeMeetingDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, sportName, regionName, COUNT(mb.memberIdx) currentCnt ");
			sb.append(" FROM bungaeMeeting bm ");
			sb.append(" LEFT OUTER JOIN memberOfBungaeMeeting mb ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
			sb.append(" LEFT OUTER JOIN sportCategory s ON bm.categoryIdx = s.sportIdx ");
			sb.append(" LEFT OUTER JOIN regionCategory r ON bm.regionIdx = r.regionIdx ");
			sb.append(" WHERE bm.bungaeMeetingIdx = ? AND mb.role != 2 ");
			sb.append(" GROUP BY bm.bungaeMeetingIdx, bm.startDate, bm.endDate, bm.place, bm.capacity, bm.subject, bm.content, bm.status, s.sportName, r.regionName");
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, bungaeMeetingIdx);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setStartDate(ts(rs, "startDate"));
                dto.setEndDate(ts(rs, "endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentCnt(rs.getInt("currentCnt"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dto;
	}

	// 주간 번개모임 조회
	public List<BungaeMeetingDTO> selectWeeklyBungaeMeetings(String keyword, String searchType) throws Exception {
	    List<BungaeMeetingDTO> list = new ArrayList<>();
	    StringBuilder sb = new StringBuilder();
	    sb.append("SELECT bm.bungaeMeetingIdx, bm.subject, bm.content, bm.startDate, bm.endDate, ");
	    sb.append("       bm.place, bm.capacity, ");
	    sb.append("       COUNT(mb.memberIdx) AS currentCnt ");
	    sb.append("FROM bungaeMeeting bm ");
	    sb.append("LEFT JOIN memberOfBungaeMeeting mb ");
	    sb.append("  ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
	    sb.append("  AND mb.role IN (0,1) "); 
	    sb.append("WHERE bm.startDate >= TRUNC(SYSDATE) ");
	    sb.append("  AND bm.startDate < TRUNC(SYSDATE) + 7 ");
	    sb.append("  AND (bm.subject LIKE ? OR bm.content LIKE ?) ");
	    sb.append("GROUP BY bm.bungaeMeetingIdx, bm.subject, bm.content, bm.startDate, bm.endDate, bm.place, bm.capacity ");
	    sb.append("ORDER BY bm.startDate ASC");

	    try (PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
	        String kw = "%" + (keyword == null ? "" : keyword) + "%";
	        pstmt.setString(1, kw);
	        pstmt.setString(2, kw);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                BungaeMeetingDTO dto = new BungaeMeetingDTO();
	                dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
	                dto.setSubject(rs.getString("subject"));
	                dto.setContent(rs.getString("content"));
	                dto.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
	                Timestamp te = rs.getTimestamp("endDate");
	                dto.setEndDate(te != null ? te.toLocalDateTime() : null);
	                dto.setPlace(rs.getString("place"));
	                dto.setCapacity(rs.getInt("capacity"));
	                dto.setCurrentCnt(rs.getInt("currentCnt"));
	                list.add(dto);
	            }
	        }
	    }
	    return list;
	}

	// 번개모임 수정
	public void updateBungaeMeeting(BungaeMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("UPDATE bungaeMeeting ");
			sb.append("SET startDate=?, endDate=?, place=?, capacity=?, subject=?, content=? ");
			sb.append("WHERE bungaeMeetingIdx = ?");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setTimestamp(1, Timestamp.valueOf(dto.getStartDate()));
			pstmt.setTimestamp(2, Timestamp.valueOf(dto.getEndDate()));
			pstmt.setString(3, dto.getPlace());
			pstmt.setInt(4, dto.getCapacity());
			pstmt.setString(5, dto.getSubject());
			pstmt.setString(6, dto.getContent());

			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 번개모임 삭제
	public void deleteBungaeMeeting(long bungaeMeetingIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM bungaeMeeting WHERE bungaeMeetingIdx = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, bungaeMeetingIdx);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 정모-> 번모 해당 회원 참여여부 확인
	public boolean isMemberAlreadyJoined(long bungaeMeetingIdx, long memberIdx) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx = ? AND memberIdx = ? AND role != 2";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setLong(1, bungaeMeetingIdx);
	        pstmt.setLong(2, memberIdx);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            return rs.next() && rs.getInt(1) > 0;
	        }
	    }
	}
	
	public void insertMember(MemberOfBungaeMeetingDTO dto) throws SQLException {
	    String sql = "INSERT INTO memberOfBungaeMeeting (bungaeMeetingIdx, memberIdx, role) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setLong(1, dto.getBungaeMeetingIdx());
	        pstmt.setLong(2, dto.getMemberIdx());
	        pstmt.setInt(3, dto.getRole());
	        pstmt.executeUpdate();
	    }
	}

	public void leaveMeeting(MemberOfBungaeMeetingDTO dto) throws SQLException {
	    String sql = "DELETE FROM memberOfBungaeMeeting WHERE bungaeMeetingIdx = ? AND memberIdx = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setLong(1, dto.getBungaeMeetingIdx());
	        pstmt.setLong(2, dto.getMemberIdx());
	        pstmt.executeUpdate();
	    }
	}

	// 정모->번모 참여 
	public boolean changeJoin(long bungaeMeetingIdx, long memberIdx) throws Exception {
	    if (isMemberAlreadyJoined(bungaeMeetingIdx, memberIdx)) {
	        
	    	MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
	    	dto.setBungaeMeetingIdx(bungaeMeetingIdx);
	    	dto.setMemberIdx(memberIdx);
	    	leaveMeeting(dto);

	        return false; 
	    } else {
	       
	        MemberOfBungaeMeetingDTO dto = new MemberOfBungaeMeetingDTO();
	        dto.setBungaeMeetingIdx(bungaeMeetingIdx);
	        dto.setMemberIdx(memberIdx);
	        dto.setRole(1); 
	        insertMember(dto);
	        return true;
	    }
	}

}

package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.BungaeMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class BungaeMeetingDAO {
	private Connection conn = DBConn.getConnection();

	public void insertBungaeMeeting(BungaeMeetingDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();

		try {
			conn.setAutoCommit(false);

			sb.append(
					"INSERT INTO bungaeMeeting(bungaeMeetingIdx, subject, content, startDate, endDate, place, capacity, status, categoryIdx, regionIdx, bungaeMemberIdx) ");
			sb.append("VALUES(bungaeMeeting_seq.NEXTVAL, ?, ?, ?, ?, ?, ?,0,?,?,?)");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getStartDate());
			pstmt.setString(4, dto.getEndDate());
			pstmt.setString(5, dto.getPlace());
			pstmt.setInt(6, dto.getCapacity());
			pstmt.setInt(7, dto.getCategoryIdx());
			pstmt.setInt(8, dto.getRegionIdx());
			pstmt.setLong(9, dto.getBungaeMemberIdx());

			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;
			sb = new StringBuilder();

			sb.append("INSERT INTO memberOfBungaeMeeting(bungaeMeetingIdx, bungaeMemberIdx) ");
			sb.append("VALUES(bungaeMeetingIdx_seq.CURRVAL, ?)");

			pstmt = conn.prepareStatement(sb.toString());

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
					"SELECT bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, sportName, regionName, bungaeMemberIdx, COUNT(mb.bungaeMemberIdx) currentMembers");
			sb.append(" FROM bungaeMeeting bm ");
			sb.append(" LEFT OUTER JOIN memberOfBungaeMeeting mb ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
			sb.append(" LEFT OUTER JOIN member1 m1 ON b.memberIdx = m1.memberIdx ");
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
					"GROUP BY bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, s.sportIdx, r.regionIdx, bungaeMemberIdx ");
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
				dto.setStartDate(rs.getString("startDate"));
				dto.setEndDate(rs.getString("endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));

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
					"SELECT bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, sportName, regionName, bungaeMemberIdx, COUNT(mb.bungaeMemberIdx) currentMembers ");
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
				dto.setStartDate(rs.getString("startDate"));
				dto.setEndDate(rs.getString("endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));

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
			sb.append(
					"SELECT bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, sportName, regionName, bungaeMemberIdx, COUNT(mb.bungaeMemberIdx) currentMembers ");
			sb.append(" FROM bungaeMeeting bm ");
			sb.append(" LEFT OUTER JOIN memberOfBungaeMeeting mb ON bm.bungaeMeetingIdx = mb.bungaeMeetingIdx ");
			sb.append(" LEFT OUTER JOIN sportCategory s ON mb.categoryIdx = s.sportIdx ");
			sb.append(" LEFT OUTER JOIN regionCategory r ON mb.regionIdx = r.regionIdx ");
			sb.append(" WHERE mb.bungaeMeetingIdx = ?");
			sb.append(
					" GROUP BY bm.bungaeMeetingIdx, startDate, endDate, place, capacity, subject, content, status, s.sportIdx, r.regionIdx, bungaeMemberIdx ");
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, bungaeMeetingIdx);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setStartDate(rs.getString("startDate"));
				dto.setEndDate(rs.getString("endDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSportName(rs.getString("sportName"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dto;
	}

	// 오늘부터 7일 이내 번개모임 조회
	public List<BungaeMeetingDTO> selectWeeklyBungaeMeetings(String keyword) throws Exception {
		List<BungaeMeetingDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT bungaeMeetingIdx, subject, content, startDate, place, capacity " + "FROM BungaeMeeting "
					+ "WHERE startDate BETWEEN SYSDATE AND SYSDATE + 7 " + "AND (subject LIKE ? OR content LIKE ?) "
					+ "ORDER BY startDate ASC";

			pstmt = conn.prepareStatement(sql);
			String kw = "%" + keyword + "%";
			pstmt.setString(1, kw);
			pstmt.setString(2, kw);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BungaeMeetingDTO dto = new BungaeMeetingDTO();
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setStartDate(rs.getString("startDate"));
				dto.setPlace(rs.getString("place"));
				dto.setCapacity(rs.getInt("capacity"));
				list.add(dto);
			}

		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
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

			pstmt.setString(1, dto.getStartDate());
			pstmt.setString(2, dto.getEndDate());

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
}

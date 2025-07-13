package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MeetingBoardDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MeetingBoardDAO {
	private Connection conn = DBConn.getConnection();

	// 데이터 추가
	public void insertMeetingBoard(MeetingBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO meetingBoard(num, memberIdx, meetingIdx, subject, content, filter, reg_date)"
					+ " VALUES (meetingBoard_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getMemberIdx());
			pstmt.setLong(2, dto.getMeetingIdx());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getFilter());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 기본 게시글 수 조회 (검색/필터 없이)
	public int dataCount(long meetingIdx) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM meetingBoard WHERE meetingIdx = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, meetingIdx);

			rs = pstmt.executeQuery();

			if (rs.next()) {
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

	// 데이터 개수
	public int dataCount(long meetingIdx, String filter) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM meetingBoard mb WHERE meetingIdx = ? AND filter = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, meetingIdx);
			pstmt.setString(2, filter);

			rs = pstmt.executeQuery();

			if (rs.next()) {
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

	// 검색 데이터 수
	public int dataCount(long meetingIdx, String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT COUNT(*) ");
			sb.append("FROM meetingBoard mb ");
			sb.append("JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append("WHERE mb.meetingIdx = ? ");

			if (schType.equals("all")) {
				sb.append("AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1) ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append("AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append("AND INSTR(" + schType + ", ?) >= 1 ");
			}

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setLong(1, meetingIdx);

			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
			} else {
				pstmt.setString(2, kwd);
			}

			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 전체 리스트 조회
	public List<MeetingBoardDTO> listMeetingBoard(long meetingIdx, int offset, int size) {
		List<MeetingBoardDTO> list = new ArrayList<MeetingBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT mb.num, mb.memberIdx, mb.meetingIdx, mb.subject, mb.content, mb.filter, ");
			sb.append(" TO_CHAR(mb.reg_date, 'YYYY-MM-DD') reg_date, m.userNickName ");
			sb.append(" FROM meetingBoard mb ");
			sb.append(" JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append(" WHERE mb.meetingIdx = ? ");
			sb.append(" ORDER BY mb.num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, meetingIdx);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingBoardDTO dto = new MeetingBoardDTO();

				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setFilter(rs.getString("filter"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUserNickName(rs.getString("userNickName"));

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

	// 리스트 + 검색
	public List<MeetingBoardDTO> listMeetingBoard(long meetingIdx, int offset, int size, String schType, String kwd) {
		List<MeetingBoardDTO> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT mb.num, mb.memberIdx, mb.meetingIdx, mb.subject, mb.content, mb.filter, ");
			sb.append("TO_CHAR(mb.reg_date, 'YYYY-MM-DD') reg_date, m.userNickName ");
			sb.append("FROM meetingBoard mb ");
			sb.append("JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append("WHERE mb.meetingIdx = ? ");

			if (kwd != null && !kwd.trim().isEmpty()) {
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(mb.subject, ?) >= 1 OR INSTR(mb.content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append("   AND TO_CHAR(mb.reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}
			}

			sb.append(" ORDER BY mb.num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			if (kwd != null && !kwd.trim().isEmpty()) {
				if (schType.equals("all")) {
					pstmt.setLong(1, meetingIdx);
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
					pstmt.setLong(4, offset);
					pstmt.setLong(5, size);

				} else {
					pstmt.setLong(1, meetingIdx);
					pstmt.setString(2, kwd);
					pstmt.setLong(3, offset);
					pstmt.setLong(4, size);
				}
			} else {
				pstmt.setLong(1, meetingIdx);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingBoardDTO dto = new MeetingBoardDTO();

				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setFilter(rs.getString("filter"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUserNickName(rs.getString("userNickName"));

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

	// 필터 조회
	public List<MeetingBoardDTO> listMeetingBoardFilter(long meetingIdx, String filter, int offset, int size,
			String schType, String kwd) {
		List<MeetingBoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT mb.num, mb.memberIdx, mb.meetingIdx, mb.subject, mb.content, mb.filter, ");
			sb.append("TO_CHAR(mb.reg_date, 'YYYY-MM-DD') reg_date, m.userNickName ");
			sb.append("FROM meetingBoard mb ");
			sb.append("JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append("WHERE mb.meetingIdx = ? AND mb.filter = ? ");

			if (kwd != null && !kwd.trim().isEmpty()) {
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(mb.subject, ?) >= 1 OR INSTR(mb.content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append("   AND TO_CHAR(mb.reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}
			}

			sb.append("ORDER BY mb.num DESC ");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, meetingIdx);
			pstmt.setString(2, filter);

			if (kwd != null && !kwd.trim().isEmpty()) {
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
					pstmt.setString(4, kwd);
					pstmt.setInt(5, offset);
					pstmt.setInt(6, size);
				} else {
					pstmt.setString(3, kwd);
					pstmt.setInt(4, offset);
					pstmt.setInt(5, size);
				}
			} else {
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingBoardDTO dto = new MeetingBoardDTO();

				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setFilter(rs.getString("filter"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUserNickName(rs.getString("userNickName"));

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

	// 게시글 보기
	public MeetingBoardDTO findById(long num) {
		MeetingBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT mb.num, mb.memberIdx, mb.meetingIdx, mb.subject, mb.content, "
					+ "mb.filter, TO_CHAR(mb.reg_date, 'YYYY-MM-DD') AS reg_date, m.userNickName "
					+ "FROM meetingBoard mb " + "JOIN member1 m ON mb.memberIdx = m.memberIdx " + "WHERE mb.num = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MeetingBoardDTO();

				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setFilter(rs.getString("filter"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setUserNickName(rs.getString("userNickName"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 글 수정
	public void updateMeetingBoard(MeetingBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE meetingBoard SET subject = ?, content = ? " + " WHERE num = ? AND memberIdx = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getNum());
			pstmt.setLong(4, dto.getMemberIdx());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 글 삭제
	public void deleteMeetingBoard(long num, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM meetingBoard WHERE num = ? AND memberIdx = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);
			pstmt.setLong(2, memberIdx);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 로그인 유저 좋아요 여부
	public boolean isUserBoardLike(long num, long memberIdx) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT num, userId FROM meetingBoardLike WHERE num = ? AND memberIdx = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);
			pstmt.setLong(2, memberIdx);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 좋아요 추가
	public void insertMeetingBoardLike(long num, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO meetingBoardLike(memberIdx, num) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, memberIdx);
			pstmt.setLong(2, num);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 좋아요 삭제
	public void deleteMeetingBoardLike(long num, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM meetingBoardLike WHERE memberIdx = ? AND num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, memberIdx);
			pstmt.setLong(2, num);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 좋아요 개수
	public int countMeetingBoardLike(long num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM meetingBoardLike WHERE num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();
			if (rs.next()) {
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

}

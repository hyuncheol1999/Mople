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
	public long insertMeetingBoard(MeetingBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		long num = 0;

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
			
			DBUtil.close(pstmt);
			sql = "SELECT meetingBoard_seq.CURRVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return num;
	}

	// 게시글 총 개수 (필터, 검색 조건 포함)
	public int dataCount(long meetingIdx, String filter, String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT COUNT(*) ");
			sb.append("FROM meetingBoard mb ");
			sb.append("JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append("WHERE mb.meetingIdx = ? ");

			if (filter != null && !filter.trim().isEmpty()) {
				sb.append(" AND mb.filter = ? ");
			}

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

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setLong(1, meetingIdx);

			if (filter != null && !filter.trim().isEmpty()) {
				if (kwd != null && !kwd.trim().isEmpty()) {
					if ("all".equals(schType)) {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, filter);
						pstmt.setString(3, kwd);
						pstmt.setString(4, kwd);
					} else {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, filter);
						pstmt.setString(3, kwd);
					}
				} else {
					pstmt.setLong(1, meetingIdx);
					pstmt.setString(2, filter);
				}
			} else {
				if (kwd != null && !kwd.trim().isEmpty()) {
					if ("all".equals(schType)) {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, kwd);
						pstmt.setString(3, kwd);
					} else {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, kwd);
					}
				} else {
					pstmt.setLong(1, meetingIdx);
				}
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

	// 게시글 리스트 조회 ((필터, 검색, 페이징)
	public List<MeetingBoardDTO> searchBoard(long meetingIdx, int offset, int size, String filter, String schType,
			String kwd) {
		List<MeetingBoardDTO> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			StringBuilder sb = new StringBuilder();

			sb.append("SELECT mb.num, mb.memberIdx, mb.meetingIdx, mb.subject, mb.content, mb.filter, ");
			sb.append("TO_CHAR(mb.reg_date, 'YYYY-MM-DD') reg_date, m.userNickName, ");
			sb.append(" (SELECT imageFileName FROM meetingBoardImg img WHERE img.num = mb.num AND ROWNUM = 1) imageFileName  ");
			sb.append("FROM meetingBoard mb ");
			sb.append("JOIN member1 m ON mb.memberIdx = m.memberIdx ");
			sb.append("WHERE mb.meetingIdx = ? ");
			
			if (filter != null && !filter.trim().isEmpty()) {
				sb.append(" AND mb.filter = ? ");
			}

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
					if (filter != null && !filter.trim().isEmpty()) {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, filter);
						pstmt.setString(3, kwd);
						pstmt.setString(4, kwd);
						pstmt.setInt(5, offset);
						pstmt.setInt(6, size);
					} else {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, kwd);
						pstmt.setString(3, kwd);
						pstmt.setInt(4, offset);
						pstmt.setInt(5, size);
					}
				} else {
					if (filter != null && !filter.trim().isEmpty()) {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, filter);
						pstmt.setString(3, kwd);
						pstmt.setInt(4, offset);
						pstmt.setInt(5, size);
					} else {
						pstmt.setLong(1, meetingIdx);
						pstmt.setString(2, kwd);
						pstmt.setInt(3, offset);
						pstmt.setInt(4, size);
					}
				}
			} else {
				if (filter != null && !filter.trim().isEmpty()) {
					pstmt.setLong(1, meetingIdx);
					pstmt.setString(2, filter);
					pstmt.setInt(3, offset);
					pstmt.setInt(4, size);
				} else {
					pstmt.setLong(1, meetingIdx);
					pstmt.setInt(2, offset);
					pstmt.setInt(3, size);
				}
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
				dto.setThumbnail(rs.getString("imageFileName"));

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
					+ "FROM meetingBoard mb " + "JOIN member1 m ON mb.memberIdx = m.memberIdx WHERE mb.num = ?";

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

	// 이미지 저장
	public void insertMeetingBoardImg(MeetingBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO meetingBoardImg(fileNum, num, imageFileName) VALUES (meetingBoardImg_seq.NEXTVAL, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			List<String> imageList = dto.getImageFileNames();
			if (imageList != null) {
				for (String fileName : imageList) {
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, fileName);
					pstmt.executeUpdate();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
	}

	// 이미지 목록 조회
	public List<String> listMeetingBoardImage(long num) throws SQLException {
		List<String> list = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT imageFileName FROM meetingBoardImg WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("imageFileName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 이전 글
	public MeetingBoardDTO findByPrev(long num, String schType, String kwd) {
		MeetingBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && !kwd.trim().isEmpty()) {
				sb.append(" SELECT mb.num, mb.subject ");
				sb.append(" FROM meetingBoard mb ");
				sb.append(" JOIN member1 m ON mb.memberIdx = m.memberIdx ");
				sb.append(" WHERE mb.num > ? ");

				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(mb.subject, ?) >= 1 OR INSTR(mb.content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append("   AND TO_CHAR(mb.reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}

				sb.append(" ORDER BY mb.num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM meetingBoard ");
				sb.append(" WHERE num > ?");
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MeetingBoardDTO();
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음 글
	public MeetingBoardDTO findByNext(long num, String schType, String kwd) {
		MeetingBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && !kwd.trim().isEmpty()) {
				sb.append(" SELECT mb.num, mb.subject ");
				sb.append(" FROM meetingBoard mb ");
				sb.append(" JOIN member1 m ON mb.memberIdx = m.memberIdx ");
				sb.append(" WHERE mb.num < ? ");

				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(mb.subject, ?) >= 1 OR INSTR(mb.content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append("   AND TO_CHAR(mb.reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}

				sb.append(" ORDER BY mb.num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM meetingBoard ");
				sb.append(" WHERE num < ?");
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MeetingBoardDTO();
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
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

	// 모임 멤버 여부
	public boolean isMeetingMember(long meetingIdx, long memberIdx) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT meetingIdx, memberIdx FROM meetingBoard WHERE meetingIdx = ? AND memberIdx = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, meetingIdx);
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

	// 로그인 유저 좋아요 여부
	public boolean isUserBoardLike(long num, long memberIdx) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT num, memberIdx FROM meetingBoardLike WHERE num = ? AND memberIdx = ? ";

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

	// 댓글 및 답글 추가
	public void insertReply(MeetingBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO meetingBoardReply(replyNum, num, memberIdx, content, reg_date, parentNum ) "
					+ " VALUES (meetingBoardReply_seq.NEXTVAL, ?, ?, ?, SYSDATE, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getNum());
			pstmt.setLong(2, dto.getMemberIdx());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getParentNum());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 댓글 개수
	public int dataCountReply(long num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM meetingBoardReply WHERE num = ? AND parentNum = 0";

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

	// 댓글 목록 (페이징, 권한 체크 포함)
	public List<MeetingBoardDTO> listReply(long num, int offset, int size, long memberIdx) {
		List<MeetingBoardDTO> list = new ArrayList<MeetingBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT mb.replyNum, mb.num, mb.memberIdx, m.userNickName, mb.content, ");
			sb.append(" TO_CHAR(mb.reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" NVL(a.answerCount, 0) answerCount  ");
			sb.append(" FROM meetingBoardReply mb ");
			sb.append(" JOIN member1 m ON m.memberIdx = mb.memberIdx ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append(" SELECT parentNum, COUNT(*) answerCount ");
			sb.append(" FROM meetingBoardReply ");
			sb.append(" WHERE parentNum != 0 ");
			sb.append(" GROUP BY parentNum ");
			sb.append(" ) a ON mb.replyNum = a.parentNum ");
			sb.append(" WHERE mb.num = ? AND mb.parentNum = 0 ");
			sb.append(" ORDER BY mb.replyNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, num);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingBoardDTO dto = new MeetingBoardDTO();

				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setAnswerCount(rs.getInt("answerCount"));

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

	// 특정 댓글 상세 정보 조회
	public MeetingBoardDTO findByReplyId(long replyNum) {
		MeetingBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT replyNum, num, memberIdx, content, reg_date FROM meetingBoardReply mb "
					+ " JOIN member1 m ON mb.memberIdx = m.memberIdx WHERE replyNum = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, replyNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MeetingBoardDTO();

				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 댓글 삭제
	public void deleteReply(long replyNum, long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {

			sql = " DELETE FROM meetingBoardReply WHERE replyNum = ? OR parentNum = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, replyNum);
			pstmt.setLong(2, replyNum);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 대댓글 목록
	public List<MeetingBoardDTO> listReplyAnswer(long parentNum, long memberIdx) {
		List<MeetingBoardDTO> list = new ArrayList<MeetingBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(
					" SELECT mb.replyNum, mb.num, mb.memberIdx, m.userNickName, mb.content, mb.reg_date, mb.parentNum ");
			sb.append(" FROM meetingBoardReply mb ");
			sb.append(" JOIN member1 m ON m.memberIdx = mb.memberIdx ");
			sb.append(" WHERE mb.parentNum = ? ");
			sb.append(" ORDER BY mb.replyNum ASC ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setLong(1, parentNum);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingBoardDTO dto = new MeetingBoardDTO();

				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setParentNum(rs.getLong("parentNum"));

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

	// 대댓글 개수
	public int dataCountReplyAnswer(long parentNum) {
		int result = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM meetingBoardReply WHERE parentNum = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, parentNum);

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

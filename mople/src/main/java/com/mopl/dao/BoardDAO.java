package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mopl.model.ReplyDTO;
import com.mopl.model.BoardDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();
	
	// 게시판 추가
	public void insertBoard(BoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO bbs(num, memberIdx, subject, content, hitCount, reg_date) "
					+ " VALUES (bbs_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getMemberIdx());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 데이터 개수
		public int dataCount() {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM bbs ";
				
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
		public int dataCount(String schType, String kwd) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) "
						+ " FROM bbs b "
						+ " JOIN member1 m ON b.memberIdx = m.memberIdx ";
				if(schType.equals("all")) {
					sql += " AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ";
				} else if(schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
				} else {
					sql += " AND INSTR(" + schType + ", ?) >= 1 ";
				}
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, kwd);
				if(schType.equals("all")) {
					pstmt.setString(2, kwd);
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

		// 게시물 리스트
		public List<BoardDTO> listBoard(int offset, int size) {
			List<BoardDTO> list = new ArrayList<BoardDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT ");
				sb.append("    b.num, ");
				sb.append("    m.userNickName, ");
				sb.append("    b.subject, ");
				sb.append("    b.hitCount, ");
				sb.append("    TO_CHAR(b.reg_date, 'YYYY-MM-DD') AS reg_date ");
				sb.append(" FROM bbs b ");
				sb.append(" JOIN member1 m ON b.memberIdx = m.memberIdx ");
				sb.append(" ORDER BY b.num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					BoardDTO dto = new BoardDTO();
					
					dto.setNum(rs.getLong("num"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date"));
					
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

		public List<BoardDTO> listBoard(int offset, int size, String schType, String kwd) {
			List<BoardDTO> list = new ArrayList<BoardDTO>();

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT b.num, userNickName, subject, hitCount, ");
				sb.append("    TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
				sb.append(" FROM bbs b");
				sb.append(" JOIN member1 m ON b.memberIdx = m.memberIdx ");
				if(schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if(schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append("   AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}
				
				sb.append(" ORDER BY num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				if(schType.equals("all")) {
					pstmt.setString(1, kwd);
					pstmt.setString(2, kwd);
					pstmt.setInt(3, offset);
					pstmt.setInt(4, size);
					
				} else {
					pstmt.setString(1, kwd);
					pstmt.setInt(2, offset);
					pstmt.setInt(3, size);
				}

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					BoardDTO dto = new BoardDTO();
					
					dto.setNum(rs.getLong("num"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date"));
					
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

		// 조회수 증가하기
		public void updateHitCount(long num) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				sql = "UPDATE bbs SET hitCount = hitCount + 1 WHERE num = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}

		// 해당 게시물 보기
		public BoardDTO findById(long num) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT b.num, b.memberIdx, m.userNickName, b.subject, b.content, "
					    + "b.reg_date, b.hitCount, NVL(bc.boardLikeCount, 0) AS boardLikeCount "
					    + "FROM bbs b "
					    + "JOIN member1 m ON b.memberIdx = m.memberIdx "
					    + "LEFT OUTER JOIN ("
					    + "SELECT bl.num, COUNT(*) AS boardLikeCount "
					    + "FROM bbsLike bl GROUP BY bl.num"
					    + ") bc ON b.num = bc.num "
					    + "WHERE b.num = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
					dto.setNum(rs.getLong("num"));
					dto.setMemberIdx(rs.getLong("memberIdx"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setSubject(rs.getString("subject"));
					dto.setContent(rs.getString("content"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setReg_date(rs.getString("reg_date"));
					
					dto.setBoardLikeCount(rs.getInt("boardLikeCount"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return dto;
		}

		// 이전글
		public BoardDTO findByPrev(long num, String schType, String kwd) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT num, subject ");
					sb.append(" FROM bbs b ");
					sb.append(" JOIN member1 m ON b.memberIdx = m.memberIdx ");
					sb.append(" WHERE num > ?  ");
					if (schType.equals("all")) {
						sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
					} else if (schType.equals("reg_date")) {
						kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
						sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
					} else {
						sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
					}
					sb.append(" ORDER BY num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					if (schType.equals("all")) {
						pstmt.setString(3, kwd);
					}
				} else {
					sb.append(" SELECT num, subject ");
					sb.append(" FROM bbs ");
					sb.append(" WHERE num > ? ");
					sb.append(" ORDER BY num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
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

		// 다음글
		public BoardDTO findByNext(long num, String schType, String kwd) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT num, subject ");
					sb.append(" FROM bbs b ");
					sb.append(" JOIN member1 m ON b.memberIdx = m.memberIdx ");
					sb.append(" WHERE num < ?  ");
					if (schType.equals("all")) {
						sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
					} else if (schType.equals("reg_date")) {
						kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
						sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
					} else {
						sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
					}
					sb.append(" ORDER BY num DESC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					if (schType.equals("all")) {
						pstmt.setString(3, kwd);
					}
				} else {
					sb.append(" SELECT num, subject ");
					sb.append(" FROM bbs ");
					sb.append(" WHERE AND num < ? ");
					sb.append(" ORDER BY num DESC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
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

		// 게시물 수정
		public void updateBoard(BoardDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql = null;
			
			try {
				sql = "UPDATE bbs SET subject = ?, content = ? WHERE num = ? AND memberIdx = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getSubject());
				pstmt.setString(2, dto.getContent());
				pstmt.setLong(3, dto.getNum());
				pstmt.setLong(4, dto.getMemberIdx());
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(pstmt);
			}

		}

		// 게시물 삭제
		public void deleteBoard(long num, long memberIdx, int role) throws SQLException {
			PreparedStatement pstmt = null;
			String sql = null;
			
			try {
				if(role == 0) {
					sql = "DELETE FROM bbs WHERE num = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, num);
					
					pstmt.executeUpdate();
				} else {
					sql = "DELETE FROM bbs WHERE num = ? AND memberIdx = ? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, num);
					pstmt.setLong(2, memberIdx);
					
					pstmt.executeUpdate();
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(pstmt);
			}

		}
		
		// 로그인 유저의 게시글 공감 유무
		public boolean isUserBoardLike(long num, long memberIdx) {
			boolean result = false;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num,memberIdx FROM bbsLike WHERE num = ? AND memberIdx = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setLong(2, memberIdx);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return result;
		}
		
		// 게시물의 공감 추가
		public void insertBoardLike(long num, long memberIdx) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO bbsLike(num,memberIdx) VALUES(?,?)";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setLong(2, memberIdx);
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(pstmt);
			}
			
			

		}
		
		// 게시글 공감 삭제
		public void deleteBoardLike(long num, long memberIdx) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "DELETE FROM bbsLike WHERE num = ? AND memberIdx = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setLong(2, memberIdx);
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(pstmt);
			}
		}
		
		// 게시물의 공감 개수
		public int countBoardLike(long num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				sql = "SELECT NVL(COUNT(*),0) FROM bbsLike WHERE num = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, num);
				
				rs= pstmt.executeQuery();
				if(rs.next()) {
					result = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			
			return result;
		}

		// 게시물의 댓글 및 답글 추가
		public void insertReply(ReplyDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO bbsReply(replyNum,num,memberIdx,content,parentNum,reg_date,showReply)"
						+ " VALUES(bbsReply_seq.NEXTVAL,?,?,?,?,SYSDATE,1)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, dto.getNum());
				pstmt.setLong(2, dto.getMemberIdx());
				pstmt.setString(3, dto.getContent());
				pstmt.setLong(4, dto.getParentNum());
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(pstmt);
			}
			
		}

		// 게시물의 댓글 개수
		public int dataCountReply(long num, Long memberIdx, int role) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM bbsReply WHERE num = ? AND parentNum = 0 ";
				
				if(role > 0) {
					sql += " AND (memberIdx = ? OR showReply = 1)";
				}
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				if(role > 0) {
					pstmt.setLong(2, memberIdx);
				}
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getInt(1);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			
			
			return result;
		}

		// 게시물 댓글 리스트
		public List<ReplyDTO> listReply(long num, int offset, int size, long memberIdx, int role) {
			List<ReplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT r.replyNum, r.memberIdx, userNickName , num, content, r.reg_date, r.showReply ,");
				sb.append(" 	NVL(answerCount, 0) answerCount, ");
				sb.append(" 	NVL(likeCount, 0) likeCount, ");
				sb.append(" 	NVL(disLikeCount, 0) disLikeCount ");
				sb.append(" FROM bbsReply r");
				sb.append(" JOIN member1 m ON r.memberIdx = m.memberIdx");
				sb.append(" LEFT OUTER JOIN (");
				sb.append(" 	SELECT parentNum,COUNT(*) answerCount ");
				sb.append("		FROM bbsReply ");
				sb.append("		WHERE parentNum != 0 ");
				if(role > 0) {
					sb.append(" 	AND (memberIdx = ? OR showReply = 1)");
				}
				sb.append("		GROUP BY parentNum ");
				sb.append(" ) a ON r.replyNum = a.parentNum ");
				sb.append(" LEFT OUTER JOIN (");
				sb.append(" 	SELECT replyNum, ");
				sb.append(" 		COUNT(DECODE(replyLike,1,1)) likeCount, ");
				sb.append(" 		COUNT(DECODE(replyLike,0,1)) disLikeCount ");
				sb.append(" 	FROM bbsReplyLike ");
				sb.append(" 	GROUP BY replyNum ");
				sb.append(" ) b ON r.replyNum = b.replyNum");
				sb.append(" WHERE num = ? AND r.parentNum = 0 ");
				if(role > 0) {
					sb.append(" AND (r.memberIdx = ? OR showReply = 1)");
				}
				sb.append(" ORDER BY r.replyNum DESC");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				if(role > 0) {
					pstmt.setLong(1, memberIdx);
					pstmt.setLong(2, num);
					pstmt.setLong(3, memberIdx);
					pstmt.setInt(4, offset);
					pstmt.setInt(5, size);
				}else {
					pstmt.setLong(1, num);
					pstmt.setInt(2, offset);
					pstmt.setInt(3, size);
				}
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ReplyDTO dto = new ReplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("num"));
					dto.setMemberIdx(rs.getLong("memberIdx"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setShowReply(rs.getInt("showReply"));
					
					dto.setAnswerCount(rs.getInt("answerCount"));
					dto.setLikeCount(rs.getInt("likeCount"));
					dto.setDisLikeCount(rs.getInt("disLikeCount"));
					
					list.add(dto);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return list;
		}

		public ReplyDTO findByReplyId(long replyNum) {
			ReplyDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT replyNum, num, r.memberIdx, userNickName, content, r.reg_date , showReply, showReply "
						+ " FROM bbsReply r"
						+ " JOIN member1 m ON r.memberIdx = m.memberIdx "
						+ " WHERE replyNum = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, replyNum);
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					dto = new ReplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("num"));
					dto.setMemberIdx(rs.getLong("memberIdx"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setShowReply(rs.getInt("showReply"));
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return dto;
		}
		
		// 게시물의 댓글 삭제
		public void deleteReply(long replyNum, long memberIdx, int role) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				if(role > 0) {
					ReplyDTO  dto = findByReplyId(replyNum);
					if(dto==null || ! (memberIdx != dto.getMemberIdx())) {
						return;
					}
				}
				
				/*
				sql = "DELETE FROM bbsReply WHERE replyNum IN (SELECT replyNum FROM bbsReply START WITH replyNum = ?"
						+ " CONNECT BY PRIOR replyNum = parentNum )";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, replyNum);
				*/
				
				if(replyNum == 0) {
					return;
				}
				sql = "DELETE FROM bbsReply WHERE replyNum = ? OR parentNum= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, replyNum);
				pstmt.setLong(2, replyNum);
				
				
				pstmt.executeUpdate();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}

		}

		// 댓글의 답글 리스트
		public List<ReplyDTO> listReplyAnswer(long parentNum, long memberIdx, int role) {
			List<ReplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT r.replyNum, r.memberIdx, userNickName , num, content, r.reg_date, parentNum, r.showReply");
				sb.append(" FROM bbsReply r");
				sb.append(" JOIN member1 m ON r.memberIdx = m.memberIdx");
				sb.append(" WHERE parentNum = ? ");
				if(role > 0) {
					sb.append(" AND (r.memberIdx = ? OR showReply = 1)");
				}
				sb.append(" ORDER BY r.replyNum DESC");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, parentNum);
				if(role > 0) {
					pstmt.setLong(2, memberIdx);
				}
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ReplyDTO dto = new ReplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("num"));
					dto.setMemberIdx(rs.getLong("memberIdx"));
					dto.setUserNickName(rs.getString("userNickName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setParentNum(rs.getLong("parentNum"));
					dto.setShowReply(rs.getInt("showReply"));
					
					list.add(dto);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			return list;
		}
		
		// 댓글의 답글 개수
		public int dataCountReplyAnswer(long parentNum,long memberIdx, long role) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM bbsReply WHERE parentNum = ? ";
				
				if(role > 0) {
					sql += " AND (memberIdx = ? OR showReply = 1)";
				}
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, parentNum);
				
				if(role > 0) {
					pstmt.setLong(2, memberIdx);
				}
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getInt(1);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return result;
		}
		
		// 댓글의 좋아요 / 싫어요 추가
		public void insertReplyLike(ReplyDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				
				sql = "INSERT INTO bbsReplyLike(replyNum, memberIdx, replyLike) VALUES(?,?,?)";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, dto.getReplyNum());
				pstmt.setLong(2, dto.getMemberIdx());
				pstmt.setInt(3, dto.getReplyLike());
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		// 댓글의 좋아요 / 싫어요 개수
		public Map<String, Integer> countReplyLike(long replyNum) {
			Map<String, Integer> map = new HashMap<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(DECODE(replyLike,1,1)) likeCount, COUNT(DECODE(replyLike,0,1)) disLikeCount "
						+ " FROM bbsReplyLike "
						+ " WHERE replyNum = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					map.put("likeCount", rs.getInt("likeCount"));
					map.put("disLikeCount", rs.getInt("disLikeCount"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return map;
		}
		// 유저의 좋아요 / 싫어요  유무 : 1-좋아요, 0-싫어요, -1:하지않은상태
		public int userReplyLike(long replyNum, long memberIdx) {
			int result = -1;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT replyLike FROM bbsReplyLike WHERE replyNum = ? AND memberIdx = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, replyNum);
				pstmt.setLong(2, memberIdx);
				
				rs= pstmt.executeQuery();
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
		
		
		// 댓글 보이기/숨기기
		public void updateReplyShowHide(long replyNum, int showReply, long memberIdx) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "UPDATE bbsReply SET showReply = ? "
						+ " WHERE replyNum = ? AND memberIdx = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, showReply);
				pstmt.setLong(2, replyNum);
				pstmt.setLong(3, memberIdx);
				
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
	// 회원이 작성한 게시글 목록
	public List<BoardDTO> findByMemberIdx(long memberIdx) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.num, b.memberIdx, m.userNickName, b.subject, b.content, "
				    + "b.reg_date, b.hitCount, NVL(bc.boardLikeCount, 0) AS boardLikeCount "
				    + "FROM bbs b "
				    + "JOIN member1 m ON b.memberIdx = m.memberIdx "
				    + "LEFT OUTER JOIN ("
				    + "SELECT bl.num, COUNT(*) AS boardLikeCount "
				    + "FROM bbsLike bl GROUP BY bl.num"
				    + ") bc ON b.num = bc.num "
				    + "WHERE b.memberIdx = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, memberIdx);

			rs = pstmt.executeQuery();

			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setBoardLikeCount(rs.getInt("boardLikeCount"));
				
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
}
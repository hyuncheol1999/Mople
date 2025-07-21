package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.BungaeReplyDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class BungaeReplyDAO {
	private Connection conn = DBConn.getConnection();
	
	// 댓글 개수
	public int dataCountReply(long replyNum, Long memberIdx, int role) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM bungaeReply WHERE replynum = ?";

			if (role > 0) {
				sql += " AND memberIdx = ?";
			}

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, replyNum);

			if (role > 0) {
				pstmt.setLong(2, memberIdx);
			}

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

	// 댓글 추가
	public void insertReply(BungaeReplyDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO bungaeReply(replyNum, content, reg_date, memberIdx, bungaeMeetingIdx) "
					+ " VALUES(bungaeReply_seq.NEXTVAL,?,SYDATE,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getReplyNum());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getMemberIdx());
			pstmt.setLong(4, dto.getBungaeMeetingIdx());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
		
	}

	// 댓글 리스트
	public List<BungaeReplyDTO> listReply(long replyNum, int offset, int size, long memberIdx, int role) {
		List<BungaeReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT r.replyNum, r.memberIdx, userNickName , r.content, r.reg_date ,");
			sb.append(" 	NVL(replyCount, 0) replyCount, ");
			sb.append(" FROM bungaeReply r");
			sb.append(" JOIN memberOfBungaeMeeting mob ON r.memberIdx = mob.memberIdx AND r.bungaeMeetingIdx = mob.bungaeMeetingIdx ");
			sb.append(" JOIN member1 m ON r.memberIdx = m.memberIdx");
			
			if (role > 0) {
				sb.append(" 	AND (memberIdx = ?)");
			}

			sb.append(" ORDER BY r.replyNum DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());

			if (role > 0) {
				pstmt.setLong(1, memberIdx);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			} else {
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BungaeReplyDTO dto = new BungaeReplyDTO();

				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setReplyCount(rs.getInt("replyCount"));

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

	public BungaeReplyDTO findByReplyNum(long replyNum) {
		BungaeReplyDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT replyNum, num, r.memberIdx, userNickName, content, r.reg_date "
					+ " FROM bbsReply r" 
					+ " JOIN member1 m ON r.memberIdx = m.memberIdx " 
					+ " JOIN memberOfBungaeMeeting mob ON r.memberIdx = mob.memberIdx AND r.bungaeMeetingIdx = mob.bungaeMeetingIdx "
					+ " WHERE replyNum = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, replyNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BungaeReplyDTO();

				dto.setReplyNum(rs.getLong("replyNum"));
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
	public void deleteReply(long replyNum, long memberIdx, int role) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (role > 0) {
				BungaeReplyDTO dto = findByReplyNum(replyNum);
				if (dto == null || !(memberIdx != dto.getMemberIdx())) {
					return;
				}
			}

			if (replyNum == 0) {
				return;
			}
			sql = "DELETE FROM bungaeReply WHERE replyNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, replyNum);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}
}

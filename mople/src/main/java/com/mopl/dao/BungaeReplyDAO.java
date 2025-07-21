package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.BungaeReplyDTO;
import com.mopl.model.MemberOfBungaeMeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class BungaeReplyDAO {
	private Connection conn = DBConn.getConnection();

	// 댓글 개수
	public int dataCountReply(long bungaeMeetingIdx, long memberIdx, int role) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM bungaeReply WHERE bungaeMeetingIdx = ?";
		if (role > 0)
			sql += " AND memberIdx = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bungaeMeetingIdx);
			if (role > 0)
				pstmt.setLong(2, memberIdx);
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

	// 댓글 추가 + 대기자 등록
	public void insertReply(BungaeReplyDTO dto) throws SQLException {
		MemberOfBungaeMeetingDAO mobDao = new MemberOfBungaeMeetingDAO();
		boolean isMember = mobDao.exists(dto.getBungaeMeetingIdx(), dto.getMemberIdx());

		// 대기 멤버 등록
		if (!isMember) {
			MemberOfBungaeMeetingDTO memberDto = new MemberOfBungaeMeetingDTO();
			memberDto.setBungaeMeetingIdx(dto.getBungaeMeetingIdx());
			memberDto.setMemberIdx(dto.getMemberIdx());
			memberDto.setRole(2); // 대기자
			mobDao.insertaBungaeMeetingMember(memberDto);
		}

		PreparedStatement pstmt = null;
		String sql = "INSERT INTO bungaeReply(replyNum, content, reg_date, memberIdx, bungaeMeetingIdx) "
				+ "VALUES(bungaeReply_seq.NEXTVAL, ?, SYSDATE, ?, ?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getContent());
			pstmt.setLong(2, dto.getMemberIdx());
			pstmt.setLong(3, dto.getBungaeMeetingIdx());
			pstmt.executeUpdate();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 댓글 목록
	public List<BungaeReplyDTO> listReply(long bungaeMeetingIdx, int offset, int size, long memberIdx, int role) {
		List<BungaeReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT r.replyNum, r.memberIdx, m.userNickName, r.content, r.reg_date ")
		   .append("FROM bungaeReply r ")
		   .append("JOIN member1 m ON r.memberIdx = m.memberIdx ")
		   .append("WHERE r.bungaeMeetingIdx = ? ")
		   .append("ORDER BY r.replyNum DESC ")
		   .append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, bungaeMeetingIdx);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BungaeReplyDTO dto = new BungaeReplyDTO();
				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setContent(rs.getString("content"));
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


	public BungaeReplyDTO findByReplyNum(long replyNum) {
		BungaeReplyDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT replyNum, content, reg_date, memberIdx, bungaeMeetingIdx FROM bungaeReply WHERE replyNum = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, replyNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BungaeReplyDTO();
				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setBungaeMeetingIdx(rs.getLong("bungaeMeetingIdx"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	public void deleteReply(long replyNum, long memberIdx, int role) throws SQLException {
		if (replyNum == 0)
			return;

		PreparedStatement pstmt = null;
		try {
			if (role > 0) {
				BungaeReplyDTO dto = findByReplyNum(replyNum);
				if (dto == null || dto.getMemberIdx() != memberIdx)
					return;
			}

			String sql = "DELETE FROM bungaeReply WHERE replyNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, replyNum);
			pstmt.executeUpdate();
		} finally {
			DBUtil.close(pstmt);
		}
	}
}
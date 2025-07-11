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

	public List<MeetingBoardDTO> listMeetingBoard(int offset, int size) {
		List<MeetingBoardDTO> list = new ArrayList<MeetingBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT num, memberIdx, meetingIdx, subject, content, filter, ");
			sb.append(" TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM meetingBoard ");
			sb.append(" JOIN ");
			sb.append("");
			sb.append("");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

}

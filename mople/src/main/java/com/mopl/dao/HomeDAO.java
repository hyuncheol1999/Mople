package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MeetingDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class HomeDAO {
	private Connection conn = DBConn.getConnection();

	// 인기 모임 가져오기 (가입자 수)
	public List<MeetingDTO> getPopularMeetings(int limit) {
		List<MeetingDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {

			sql = "SELECT m.meetingidx, m.meetingName, m.meetingProfilePhoto, r.regionName, s.sportName, "
					+ "COUNT(mm.memberIdx) currentMembers " + "FROM meeting m "
					+ "LEFT JOIN memberOfMeeting mm ON m.meetingIdx = mm.meetingIdx "
					+ "LEFT JOIN regionCategory r ON m.regionIdx = r.regionIdx "
					+ "LEFT JOIN sportCategory s ON m.sportIdx = s.sportIdx "
					+ "GROUP BY m.meetingIdx, m.meetingName, m.meetingProfilePhoto, r.regionName, s.sportName "
					+ "ORDER BY currentMembers DESC " + "FETCH FIRST ? ROWS ONLY";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, limit);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingDTO dto = new MeetingDTO();
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMeetingName(rs.getString("meetingName"));
				dto.setMeetingProfilePhoto(rs.getString("meetingProfilePhoto"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));

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

	// 인기 모임 더보기
	public List<MeetingDTO> getPopularMeetings(int offset, int limit) {
		List<MeetingDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {

			sql = "SELECT m.meetingidx, m.meetingName, m.meetingProfilePhoto, r.regionName, s.sportName, "
					+ "COUNT(mm.memberIdx) currentMembers " + "FROM meeting m "
					+ "LEFT JOIN memberOfMeeting mm ON m.meetingIdx = mm.meetingIdx "
					+ "LEFT JOIN regionCategory r ON m.regionIdx = r.regionIdx "
					+ "LEFT JOIN sportCategory s ON m.sportIdx = s.sportIdx "
					+ "GROUP BY m.meetingIdx, m.meetingName, m.meetingProfilePhoto, r.regionName, s.sportName "
					+ "ORDER BY currentMembers DESC " + "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, limit);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MeetingDTO dto = new MeetingDTO();
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMeetingName(rs.getString("meetingName"));
				dto.setMeetingProfilePhoto(rs.getString("meetingProfilePhoto"));
				dto.setCurrentMembers(rs.getInt("currentMembers"));
				dto.setRegionName(rs.getString("regionName"));
				dto.setSportName(rs.getString("sportName"));

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

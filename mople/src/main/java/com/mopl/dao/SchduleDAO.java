package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mopl.model.GameDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class SchduleDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertSchdule(GameDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO MatchSchedule( matchId, matchDate, startTime, homeTeam, awayTeam, result , stadium, status, sportsIdx)"
					+ "VALUES(Match_seq.NEXTCAL, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ,?, ? ,? , ? , ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(2, dto.getTime());
			pstmt.setString(3, dto.getHome());
			pstmt.setString(4, dto.getAway());
			String score=dto.getAwayScore()+" : "+dto.getHomeScore();
			pstmt.setString(5, score);
			pstmt.setString(6, dto.getPlace());
			pstmt.setString(7, dto.getState());
			// dto에 스포츠 idx+matchDate 추가하기
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
}

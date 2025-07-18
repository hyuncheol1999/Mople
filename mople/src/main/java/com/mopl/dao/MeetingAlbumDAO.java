package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.MeetingAlbumDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;


public class MeetingAlbumDAO {
	private Connection conn = DBConn.getConnection();
	
	// 모임 앨범 리스트
	public List<MeetingAlbumDTO> findByMeeetingIdx(long meetingIdx) {
		List<MeetingAlbumDTO> list = new ArrayList<MeetingAlbumDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT photoNum, imageFileName, content, meetingIdx, a.memberIdx, userNickName ");
			sb.append("FROM meetingAlbum a ");
			sb.append("LEFT OUTER JOIN member1 m ON a.memberIdx = m.memberIdx ");
			sb.append("WHERE meetingIdx = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, meetingIdx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MeetingAlbumDTO dto = new MeetingAlbumDTO();
				
				dto.setPhotoNum(rs.getLong("photoNum"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setContent(rs.getString("content"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				
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
	
	// 회원이 올린 모임 사진 리스트
	public List<MeetingAlbumDTO> findByMemberIdx(long memberIdx) {
		List<MeetingAlbumDTO> list = new ArrayList<MeetingAlbumDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT photoNum, imageFileName, content, a.meetingIdx, a.memberIdx, userNickName, meetingName ");
			sb.append("FROM meetingAlbum a ");
			sb.append("LEFT OUTER JOIN member1 m ON a.memberIdx = m.memberIdx ");
			sb.append("LEFT OUTER JOIN meeting mt ON a.memberIdx = mt.meetingIdx ");
			sb.append("WHERE a.memberIdx = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, memberIdx);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MeetingAlbumDTO dto = new MeetingAlbumDTO();
				
				dto.setPhotoNum(rs.getLong("photoNum"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setContent(rs.getString("content"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				
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
	
	// 사진 정보
	public MeetingAlbumDTO findByPhotoNum(long photoNum) {
		MeetingAlbumDTO dto = new MeetingAlbumDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT photoNum, imageFileName, content, meetingIdx, a.memberIdx, userNickName ");
			sb.append("FROM meetingAlbum a ");
			sb.append("LEFT OUTER JOIN member1 m ON a.memberIdx = m.memberIdx ");
			sb.append("WHERE photoNum = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, photoNum);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				dto.setPhotoNum(rs.getLong("photoNum"));
				dto.setImageFileName(rs.getString("imageFileName"));
				dto.setContent(rs.getString("content"));
				dto.setMeetingIdx(rs.getLong("meetingIdx"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserNickName(rs.getString("userNickName"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public void insertAlbum(MeetingAlbumDTO dto) {
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("INSERT INTO meetingAlbum(photoNum, imageFileName, content, meetingIdx, memberIdx) ");
			sb.append("VALUES(meetingAlbum_seq.NEXTVAL, ?, ?, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getImageFileName());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getMeetingIdx());
			pstmt.setLong(4, dto.getMemberIdx());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void deleteImage(long photoNum) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM meetingAlbum WHERE photoNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, photoNum);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
}

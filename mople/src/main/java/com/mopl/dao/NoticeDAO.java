package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mopl.model.NoticeDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;
import com.mopl.util.MyMultipartFile;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		long seq;
		
		try {
			sql = "SELECT notice_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			if(rs.next()) {
				seq = rs.getLong(1);
			}
			dto.setNum(seq);
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "INSERT INTO notice(num, notice, memberIdx, subject, content, hitCount, reg_date, modify_date, showNotice) VALUES(?, ?, ?, ?, ?, 0, SYSDATE, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getNum());
			pstmt.setInt(2, dto.getNotice());
			pstmt.setLong(3, dto.getMemberIdx());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getListFile().size() != 0) {
				sql = "INSERT INTO noticeFile(fileNum, num, saveFilename, originalFilename, fileSize) "
						+ " VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);

				for(MyMultipartFile mf: dto.getListFile()) {
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					pstmt.setLong(4, mf.getSize());
					
					pstmt.executeUpdate();
				}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM notice";
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
	
	// 검색에서 전체 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM notice n "
					+ " JOIN member1 m ON n.memberIdx = m.memberIdx ";
			if(schType.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
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
	
	public List<NoticeDTO> listNotice(int offset, int size){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, n.memberIdx, userId, userName, userNickName, ");
			sb.append("       subject, hitCount, reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
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

	public List<NoticeDTO> listNotice(int offset, int size, String schType, String kwd){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, n.memberIdx, userId, userName, userNickName, ");
			sb.append("       subject, hitCount, reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			if (schType.equals("all")) {
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
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
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
}

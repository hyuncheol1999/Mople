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
			
			sql = "INSERT INTO notice(num, notice, memberIdx, subject, content, hitCount, reg_date, modify_date, showNotice) VALUES(?, ?, ?, ?, ?, 0, SYSDATE, SYSDATE, 0)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getNum());
			pstmt.setInt(2, dto.getNotice());
			pstmt.setLong(3, dto.getMemberIdx());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			
			pstmt.executeUpdate();
			
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
			
		} catch (SQLException e) {
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
				sql += "  WHERE TO_CHAR(n.reg_date, 'YYYYMMDD') = ? ";
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	
		return result;
	}
	
	// 게시물 리스트
	public List<NoticeDTO> listNotice(int offset, int size){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, n.memberIdx, userId, userName, userNickName, ");
			sb.append("       subject, hitCount, n.reg_date ");
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}

	// 검색에서 리스트
	public List<NoticeDTO> listNotice(int offset, int size, String schType, String kwd){
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, n.memberIdx, userId, userName, userNickName, ");
			sb.append("       subject, hitCount, n.reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(n.reg_date, 'YYYYMMDD') = ?");
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	// 공지글
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, n.memberIdx, userId, userName, userNickName, subject, ");
			sb.append("       hitCount, TO_CHAR(n.reg_date, 'YYYY-MM-DD') reg_date  ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
			sb.append(" WHERE notice = 1  ");
			sb.append(" ORDER BY num DESC ");
			
			pstmt = conn.prepareStatement(sb.toString());

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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	public NoticeDTO findById(long num) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, notice, n.memberIdx, userId, userName, userNickName, subject, content, "
					+ " hitCount, n.reg_date, n.modify_date "
					+ " FROM notice n "
					+ " JOIN member1 m ON n.memberIdx = m.memberIdx "
					+ " WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setNotice(rs.getInt("notice"));
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setModify_date(rs.getString("modify_date"));
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
	public NoticeDTO findByPrev(long num, String schType, String kwd) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
				sb.append(" WHERE ( num > ? ) ");
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
				sb.append(" SELECT num, subject FROM notice ");
				sb.append(" WHERE num > ? ");
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
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
	public NoticeDTO findByNext(long num, String schType, String kwd) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN member1 m ON n.memberIdx = m.memberIdx ");
				sb.append(" WHERE ( num < ? ) ");
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
				sb.append(" SELECT num, subject FROM notice ");
				sb.append(" WHERE num > ? ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
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
	
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE notice SET hitCount = hitCount + 1 WHERE num = ?";
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
	
	public void updateNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE notice SET notice = ?, subject = ?, content = ?, modify_date = SYSDATE "
					+ " WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getNum());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;

			if (dto.getListFile() != null && dto.getListFile().size() != 0) {
				sql = "INSERT INTO noticeFile(fileNum, num, saveFilename, originalFilename, fileSize) "
						+ " VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (MyMultipartFile mf: dto.getListFile()) {
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, mf.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					pstmt.setLong(4, mf.getSize());
					
					pstmt.executeUpdate();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void deleteNotice(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM notice WHERE num = ? ";
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
	
	public void deleteNotice(long[] nums) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			/*
			sql = "DELETE FROM notice WHERE num IN (";
			for (int i = 0; i < nums.length; i++) {
				sql += "?,";
			}
			sql = sql.substring(0, sql.length() - 1) + ")";

			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < nums.length; i++) {
				pstmt.setLong(i + 1, nums[i]);
			}
			pstmt.executeUpdate();
			 */

			sql = "DELETE FROM notice WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			for(long num : nums) {
				pstmt.setLong(1, num);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	
	}
	
	public List<NoticeDTO> listNoticeFile(long num){
		List<NoticeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, saveFilename, originalFilename, fileSize "
					+ " FROM noticeFile WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setFileNum(rs.getLong("fileNum"));
				dto.setNum(rs.getLong("num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("fileSize"));
				
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
	
	public NoticeDTO findByFileId(long fileNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, saveFilename, originalFilename, fileSize "
					+ " FROM noticeFile WHERE fileNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fileNum);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();

				dto.setFileNum(rs.getLong("fileNum"));
				dto.setNum(rs.getLong("num"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("fileSize"));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public void deleteNoticeFile(String mode, long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (mode.equals("all")) {
				sql = "DELETE FROM noticeFile WHERE num = ?";
			} else {
				sql = "DELETE FROM noticeFile WHERE fileNum = ?";
			}
			
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
}


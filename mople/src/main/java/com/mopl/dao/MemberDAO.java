package com.mopl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mopl.model.MemberDTO;
import com.mopl.util.DBConn;
import com.mopl.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();

	public MemberDTO loginMember(String userId, String userPwd) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT m1.memberIdx, m1.userId, userPwd, userName, userNickName, birth, address, gender, reg_date, role, email, tel, profilePhoto"
					+ " FROM member1 m1 LEFT OUTER JOIN member2 m2 ON m1.memberIdx = m2.memberIdx "
					+ " WHERE m1.userId = ? AND userPwd = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddress(rs.getString("address"));
				dto.setGender(rs.getInt("gender"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				dto.setProfilePhoto(rs.getString("profilePhoto"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}

		return dto;
	}

	public void insertMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member1(memberIdx, userId, userPwd, userName, userNickName, birth, address, gender, reg_date, role)"
					+ " VALUES(member_seq.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, SYSDATE, 1)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getUserNickName());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getAddress());
			pstmt.setInt(7, dto.getGender());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO member2(memberIdx, email, tel, profilePhoto)"
					+ " VALUES(member_seq.CURRVAL, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getProfilePhoto());
			
			
			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);				
			} catch (Exception e2) {
			}
		}
	}

	public MemberDTO findByMemberIdx(long memberIdx) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.memberIdx, userId, userPwd, userName, userNickName, ");
			sb.append("  TO_CHAR(birth, 'YYYY-MM-DD') birth, address, gender, ");
			sb.append("  reg_date, role, email, tel, profilePhoto ");
			sb.append("FROM member1 m1 LEFT OUTER JOIN member2 m2 ON m1.memberIdx = m2.memberIdx ");
			sb.append("WHERE m1.memberIdx = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, memberIdx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddress(rs.getString("address"));
				dto.setGender(rs.getInt("gender"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				dto.setProfilePhoto(rs.getString("profilePhoto"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public MemberDTO findById(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.memberIdx, userId, userPwd, userName, userNickName, ");
			sb.append("  TO_CHAR(birth, 'YYYY-MM-DD') birth, address, gender, ");
			sb.append("  reg_date, role, email, tel, profilePhoto ");
			sb.append("FROM member1 m1 LEFT OUTER JOIN member2 m2 ON m1.memberIdx = m2.memberIdx ");
			sb.append("WHERE m1.userId = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setMemberIdx(rs.getLong("memberIdx"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddress(rs.getString("address"));
				dto.setGender(rs.getInt("gender"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setRole(rs.getInt("role"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				dto.setProfilePhoto(rs.getString("profilePhoto"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	public void updateMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			sql = "UPDATE member1 SET userPwd = ?, userNickName = ?, address = ? WHERE memberIdx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getUserNickName());
			pstmt.setString(3, dto.getAddress());
			pstmt.setLong(4, dto.getMemberIdx());

			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			sql = "UPDATE member2 SET email = ?, profilePhoto = ?, tel = ? WHERE memberIdx = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getProfilePhoto());
			pstmt.setString(3, dto.getTel());
			pstmt.setLong(4, dto.getMemberIdx());

			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			DBUtil.close(pstmt);
			DBUtil.rollback(conn);
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}

	}

	public void deleteProfilePhoto(long memberIdx) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE member2 SET profilePhoto = null WHERE memberIdx = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, memberIdx);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
   // 총 회원 수 
   public int countMember() {
      int result = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      String sql;

      try {
         sql = "SELECT NVL(COUNT(*), 0) FROM member1";
         pstmt = conn.prepareStatement(sql);

         rs = pstmt.executeQuery();
         
         if (rs.next()) {
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


   // 오늘 가입자 수
   public int countTodayMember() {
      int result = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      String sql;

      try {
         sql = "SELECT NVL(COUNT(*), 0) FROM member1 WHERE reg_date >= TRUNC(SYSDATE) AND reg_date < TRUNC(SYSDATE + 1)";
         pstmt = conn.prepareStatement(sql);

         rs = pstmt.executeQuery();
         
         if (rs.next()) {
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

}

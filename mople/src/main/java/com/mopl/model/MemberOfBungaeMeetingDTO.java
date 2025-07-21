package com.mopl.model;

public class MemberOfBungaeMeetingDTO {
	private long memberIdx;
	private long bungaeMeetingIdx;
	private int role;
	
	private String memberName;
	private String memberNickName;
	private String memberProfilePhoto;
	
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberNickName() {
		return memberNickName;
	}
	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}
	public String getMemberProfilePhoto() {
		return memberProfilePhoto;
	}
	public void setMemberProfilePhoto(String memberProfilePhoto) {
		this.memberProfilePhoto = memberProfilePhoto;
	}
	public long getMemberIdx() {
		return memberIdx;
	}
	public void setMemberIdx(long memberIdx) {
		this.memberIdx = memberIdx;
	}
	public long getBungaeMeetingIdx() {
		return bungaeMeetingIdx;
	}
	public void setBungaeMeetingIdx(long bungaeMeetingIdx) {
		this.bungaeMeetingIdx = bungaeMeetingIdx;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
	
}

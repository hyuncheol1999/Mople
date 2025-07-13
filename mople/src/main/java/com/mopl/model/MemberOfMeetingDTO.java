package com.mopl.model;

public class MemberOfMeetingDTO {
	private long meetingIdx;
	private long memberIdx;
	private int role;
	
	private String memberName;
	private String memberProfilePhoto;
	
	public long getMeetingIdx() {
		return meetingIdx;
	}
	public void setMeetingIdx(long meetingIdx) {
		this.meetingIdx = meetingIdx;
	}
	public long getMemberIdx() {
		return memberIdx;
	}
	public void setMemberIdx(long memberIdx) {
		this.memberIdx = memberIdx;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberProfilePhoto() {
		return memberProfilePhoto;
	}
	public void setMemberProfilePhoto(String memberProfilePhoto) {
		this.memberProfilePhoto = memberProfilePhoto;
	}
	
}

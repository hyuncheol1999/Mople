package com.mopl.model;

public class MeetingDTO {
	private long meetingIdx;
	private String meetingName;
	private String meetingDesc;
	private String createdDate;
	private String meetingProfilePhoto;
	private int regionIdx;
	private int sportIdx;
	
	private String regionName;
	private String sportName;
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}
	private int photoNum;
	private String imageFileName;
	private String content;
	
	private long memberIdx;
	private int role;
	public long getMeetingIdx() {
		return meetingIdx;
	}
	public void setMeetingIdx(long meetingIdx) {
		this.meetingIdx = meetingIdx;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getMeetingDesc() {
		return meetingDesc;
	}
	public void setMeetingDesc(String meetingDesc) {
		this.meetingDesc = meetingDesc;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getMeetingProfilePhoto() {
		return meetingProfilePhoto;
	}
	public void setMeetingProfilePhoto(String meetingProfilePhoto) {
		this.meetingProfilePhoto = meetingProfilePhoto;
	}
	public int getRegionIdx() {
		return regionIdx;
	}
	public void setRegionIdx(int regionIdx) {
		this.regionIdx = regionIdx;
	}
	public int getSportIdx() {
		return sportIdx;
	}
	public void setSportIdx(int sportIdx) {
		this.sportIdx = sportIdx;
	}
	public int getPhotoNum() {
		return photoNum;
	}
	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
}

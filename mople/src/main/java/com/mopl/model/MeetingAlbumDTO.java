package com.mopl.model;

public class MeetingAlbumDTO {
	private long photoNum;
	private String imageFileName;
	private String content;
	private long meetingIdx;
	private long memberIdx;
	private String userNickName;
	
	private String meetingName;
	
	public long getPhotoNum() {
		return photoNum;
	}
	public void setPhotoNum(long photoNum) {
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
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
}

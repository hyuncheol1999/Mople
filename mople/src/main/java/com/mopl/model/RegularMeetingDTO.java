package com.mopl.model;

public class RegularMeetingDTO {
	private Long regularMeetingIdx;	
	private String startDate;
	private String endDate;
	private String place;
	private int capacity;
	private String subject;
	private String content;
	private int status;
	private int isBungaeMeeting;
	
	private int sportIdx;
	private int regionIdx;
	private Long meetingIdx;
	private Long memberIdx;
	
	private int currentCnt;
    private boolean joined;

	
	
	public int getCurrentCnt() {
		return currentCnt;
	}
	public void setCurrentCnt(int currentCnt) {
		this.currentCnt = currentCnt;
	}
	public boolean isJoined() {
		return joined;
	}
	public void setJoined(boolean joined) {
		this.joined = joined;
	}
	public Long getRegularMeetingIdx() {
		return regularMeetingIdx;
	}
	public void setRegularMeetingIdx(Long regularMeetingIdx) {
		this.regularMeetingIdx = regularMeetingIdx;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsBungaeMeeting() {
		return isBungaeMeeting;
	}
	public void setIsBungaeMeeting(int isBungaeMeeting) {
		this.isBungaeMeeting = isBungaeMeeting;
	}
	public int getSportIdx() {
		return sportIdx;
	}
	public void setSportIdx(int sportIdx) {
		this.sportIdx = sportIdx;
	}
	public int getRegionIdx() {
		return regionIdx;
	}
	public void setRegionIdx(int regionIdx) {
		this.regionIdx = regionIdx;
	}
	public Long getMeetingIdx() {
		return meetingIdx;
	}
	public void setMeetingIdx(Long meetingIdx) {
		this.meetingIdx = meetingIdx;
	}
	public Long getMemberIdx() {
		return memberIdx;
	}
	public void setMemberIdx(Long memberIdx) {
		this.memberIdx = memberIdx;
	}
	
	
	
}

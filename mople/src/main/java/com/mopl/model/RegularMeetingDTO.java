package com.mopl.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class RegularMeetingDTO {
	private Long regularMeetingIdx;	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
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
    
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
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
	
	public String getStartDateDow() {
        if (startDate == null) return "";
        return startDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA);  
    }

    public int getStartDateDay() {
        return startDate != null ? startDate.getDayOfMonth() : 0;
    }

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public String getStartTimeStr() {
        return startDate != null ? startDate.toLocalTime().format(TIME_FMT) : "";
    }

    public String getEndTimeStr() {
        return endDate != null ? endDate.toLocalTime().format(TIME_FMT) : "";
    }  
	
    public String getStartDateOnly() { return startDate != null ? startDate.toLocalDate().toString() : ""; }
    public String getStartTimeOnly() { return startDate != null ? startDate.toLocalTime().toString() : ""; }
    public String getEndDateOnly()   { return endDate != null ? endDate.toLocalDate().toString() : ""; }
    public String getEndTimeOnly()   { return endDate != null ? endDate.toLocalTime().toString() : ""; }
    
}

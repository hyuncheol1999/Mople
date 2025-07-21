package com.mopl.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BungaeMeetingDTO {
	private Long bungaeMeetingIdx;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String place;
	private int capacity;
	private String subject;
	private String content;
	private int status;
	
	private int categoryIdx;
	private int regionIdx;
	private Long bungaeMemberIdx;
	
	private String sortBy;
	private String regionName;
	private String sportName;
	private int currentCnt;
	
	
	public Long getBungaeMemberIdx() {
		return bungaeMemberIdx;
	}
	public void setBungaeMemberIdx(Long bungaeMemberIdx) {
		this.bungaeMemberIdx = bungaeMemberIdx;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
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
	public Long getBungaeMeetingIdx() {
		return bungaeMeetingIdx;
	}
	public void setBungaeMeetingIdx(Long bungaeMeetingIdx) {
		this.bungaeMeetingIdx = bungaeMeetingIdx;
	}
	
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
	public int getCategoryIdx() {
		return categoryIdx;
	}
	public void setCategoryIdx(int categoryIdx) {
		this.categoryIdx = categoryIdx;
	}
	public int getRegionIdx() {
		return regionIdx;
	}
	public void setRegionIdx(int regionIdx) {
		this.regionIdx = regionIdx;
	}
	public String getStartDateDay() {
	    return startDate!=null ? String.format("%02d", startDate.getDayOfMonth()) : "";
	}
	public String getStartTimeStr() {
	    return startDate!=null ? startDate.toLocalTime().toString().substring(0,5) : "";
	}
	public String getEndTimeStr() {
	    return endDate!=null ? endDate.toLocalTime().toString().substring(0,5) : "";
	}
	public boolean isMultiDay() {
	    return startDate!=null && endDate!=null &&
	           (startDate.toLocalDate().isBefore(endDate.toLocalDate()));
	}
	public Timestamp getStartDateAsDate() {
	    return startDate == null ? null : java.sql.Timestamp.valueOf(startDate);
	}
	public Timestamp getEndDateAsDate() {
	    return endDate == null ? null : java.sql.Timestamp.valueOf(endDate);
	}
	public int getCurrentCnt() {
		return currentCnt;
	}
	public void setCurrentCnt(int currentCnt) {
		this.currentCnt = currentCnt;
	}

	
	
}

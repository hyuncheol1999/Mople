package com.mopl.model;

public class BungaeMeetingDTO {
	private Long bungaeMeetingIdx;
	private String startDate;
	private String endDate;
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
	private int currentMembers;
	
	
	
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
	public int getCurrentMembers() {
		return currentMembers;
	}
	public void setCurrentMembers(int currentMembers) {
		this.currentMembers = currentMembers;
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

	
	
}

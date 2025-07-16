package com.mopl.model;

public class MeetingBoardDTO {
	private long num;
	private long memberIdx;
	private long meetingIdx;

	private String subject;
	private String content;
	private String filter;
	private String reg_date;
	private String userNickName;

	private long fileNum;
	private String imageFileName;

	private long replyNum;
	private long parentNum;

	private int answerCount;

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public long getFileNum() {
		return fileNum;
	}

	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public long getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(long replyNum) {
		this.replyNum = replyNum;
	}

	public long getParentNum() {
		return parentNum;
	}

	public void setParentNum(long parentNum) {
		this.parentNum = parentNum;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getMemberIdx() {
		return memberIdx;
	}

	public void setMemberIdx(long memberIdx) {
		this.memberIdx = memberIdx;
	}

	public long getMeetingIdx() {
		return meetingIdx;
	}

	public void setMeetingIdx(long meetingIdx) {
		this.meetingIdx = meetingIdx;
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

}

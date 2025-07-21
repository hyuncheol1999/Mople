package com.mopl.model;

public class BungaeReplyDTO {
	private Long replyNum;
	private String content;
	private String reg_date;
	private Long memberIdx;
	private Long bungaeMeetingIdx;
	
	private String userNickName;
	private int replyCount;
	
	public Long getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(Long replyNum) {
		this.replyNum = replyNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Long getMemberIdx() {
		return memberIdx;
	}
	public void setMemberIdx(Long memberIdx) {
		this.memberIdx = memberIdx;
	}
	public Long getBungaeMeetingIdx() {
		return bungaeMeetingIdx;
	}
	public void setBungaeMeetingIdx(Long bungaeMeetingIdx) {
		this.bungaeMeetingIdx = bungaeMeetingIdx;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	
	
	
	
	
}

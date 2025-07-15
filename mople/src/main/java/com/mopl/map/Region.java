package com.mopl.map;

public class Region {
	private int num;
	private String subject;
	private String addr;
	private String other;
	private String imageFilename;
	private double latitude; //  래터튜드(위도)
	private double longitude; // 란저튜드(경도)
	
	public Region() {
	}
	public Region(int num, String subject, String addr, String other, String imageFilename, double latitude, double longitude) {
		this.num = num;
		this.subject = subject;
		this.addr = addr;
		this.other = other;
		this.imageFilename = imageFilename;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getImageFilename() {
		return imageFilename;
	}
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}

package com.carrie.lldiary.entity;

public class AnnEntity {
	private int image;//标识类型图片
	private String content;
	private String date;
	private String duringTime;//过了多少天
	private String isRemind;//是否提醒,"提醒"，”不提醒“
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDuringTime() {
		return duringTime;
	}
	public void setDuringTime(String duringTime) {
		this.duringTime = duringTime;
	}
	public String getIsRemind() {
		return isRemind;
	}
	public void setIsRemind(String isRemind) {
		this.isRemind = isRemind;
	}
	
	
	
	

}

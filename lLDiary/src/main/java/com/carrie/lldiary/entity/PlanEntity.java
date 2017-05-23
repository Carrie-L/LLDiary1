package com.carrie.lldiary.entity;

public class PlanEntity {
	private String content;//提醒正文
	private String date;//发表日期
	private String time;//发表的时间
	private String isComplish;//是否完成
	private String isRemind;//是否提醒

	
	
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIsComplish() {
		return isComplish;
	}
	public void setIsComplish(String isComplish) {
		this.isComplish = isComplish;
	}
	public String getIsRemind() {
		return isRemind;
	}
	public void setIsRemind(String isRemind) {
		this.isRemind = isRemind;
	}

	
	
	
	
}

package com.carrie.lldiary.entity;

import java.sql.Date;

public class ContentEntity {
	private String Title;//日记标题
	private String content;//正文
	private String date;//发表日期
	private int background;//日记背景
	
	public ContentEntity(String title, String content, String date,
			int background) {
		super();
		Title = title;
		this.content = content;
		this.date = date;
		this.background = background;
	}
	
	
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
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
	public int getBackground() {
		return background;
	}
	public void setBackground(int background) {
		this.background = background;
	}


	

}

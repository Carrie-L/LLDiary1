package com.carrie.lldiary.entity;

import android.graphics.drawable.Drawable;

public class RobotEntity {
	/** 接收机器人发送的消息*/
	public static final int RECEIVER=1;
	/**用户发送出去的消息*/
	public static final int SEND=2;  
	private String content;
	/**消息的类型为接收还是发送*/
	private int type;	
	private String time;
	private String photo;
	
	
	public RobotEntity(String content, int type, String time) {
		setContent(content);
		setType(type);
		setTime(time);
		
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public static int getReceiver() {
		return RECEIVER;
	}
	public static int getSend() {
		return SEND;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	

}

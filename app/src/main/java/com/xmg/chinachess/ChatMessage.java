package com.xmg.chinachess;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {
	private String from;
	private String content;
	private String sendTime;

	public ChatMessage(String from, String content, String sendTime) {
		super();
		this.from = from;
		this.content = content;
		this.sendTime = sendTime;
	}

	public ChatMessage(String from, String content) {
		super();
		this.from = from;
		this.content = content;
	}

	public ChatMessage() {
		super();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd hh:MM:ss");
		Date d = new Date();
		sendTime = sdf.format(d);
		return User.encode(from) + "," + User.encode(content) + ","
				+ User.encode(sendTime);
	}

	public static ChatMessage fromString(String data) {
		String[] tag = data.split(",");
		ChatMessage cm = new ChatMessage();
		String from = User.decode(tag[0]);
		String content = User.decode(tag[1]);
		String sendTime = User.decode(tag[2]);
		return new ChatMessage(from, content, sendTime);
	}
}

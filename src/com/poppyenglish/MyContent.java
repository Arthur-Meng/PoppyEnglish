package com.poppyenglish;

public class MyContent {
	private static MyContent instance = new MyContent();
	private String[] content;
	private String contentAll;
	private Boolean ifReady = false;

	private MyContent() {
	}

	public static MyContent getInstance() {
		return instance;
	}

	public void setContentAll(String msg) {
		this.contentAll = msg;
	}

	public String getContentAll() {
		return this.contentAll;
	}
	
	public void setContent(String[] msg) {
		this.content = msg;
	}

	public String[] getContent() {
		content=contentAll.split(":");
		return this.content;
	}

	public void setIfReady(boolean tf) {
		this.ifReady = tf;
	}

	public Boolean getIfReady() {
		return this.ifReady;
	}
}

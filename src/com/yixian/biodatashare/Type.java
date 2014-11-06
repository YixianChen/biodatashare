package com.yixian.biodatashare;

public class Type {
	 
	private String type; 
	Type(String type){
		this.type = type;
	}
	
	public String toString() {
		return "{\"type\":\""+type+"\"}";
	}
	
	
}

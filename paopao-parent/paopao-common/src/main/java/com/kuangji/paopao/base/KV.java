package com.kuangji.paopao.base;

import lombok.Data;

//KEY VALUE 对象
@Data
public class KV {

	private String Name;
	
	private int   value;

	public KV(String name, int value) {
		super();
		Name = name;
		this.value = value;
	}
	
	
	public KV() {

	}
	
	
}

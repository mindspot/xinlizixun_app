package com.kuangji.paopao.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 年龄段类的枚举类型
 * 
 */
public enum AgeGroupEnum {
	SIXTY("60后", 196), 
	SEVENTY("70后", 197), 
	EIGHTY("80后", 198),
	NINETY("90后", 199);
	// 成员变量
	private String index;

	private int value;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private AgeGroupEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}
	
	
	public static List<Integer> ages(Integer[] ageGroups ) {
		
		List<Integer> ages =new ArrayList<Integer>();
		
		for (Integer i : ageGroups) {
			
		}
		
		return null;
		
	}
	
	
	protected  static  List<Integer> ages(int  age){
		
		return null;
		
		
	}
}

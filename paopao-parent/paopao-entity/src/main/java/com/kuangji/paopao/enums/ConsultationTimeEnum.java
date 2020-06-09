package com.kuangji.paopao.enums;

import java.util.ArrayList;
import java.util.List;

import com.kuangji.paopao.model.Common;

/**
 * Author 金威正 Date 2020-02-27
 */

public enum ConsultationTimeEnum {
	WORKING_DAY_BEFOR_DAWN("凌晨", 10000, 1),
	WORKING_DAY_MORNING("上午", 10001, 2), 
	WORKING_DAY_AFTERNOON("下午", 10002,3), 
	WORKING_DAY_NIGHT("晚间", 10003, 4);
	// 成员变量
	private String index;

	private int value;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

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

	private ConsultationTimeEnum(String index, int value, int code) {
		this.index = index;
		this.value = value;
		this.code = code;
	}

	public static List<Common> listConsultationTimeEnum() {
		List<Common> list = new ArrayList<Common>();
		for (ConsultationTimeEnum em : ConsultationTimeEnum.values()) {
			Common common = new Common();
			common.setVal(em.getIndex());
			common.setValCode(em.code);
			list.add(common);
		}
		return list;
	}

	public static int getConsultationTimeEnumCode(int value ) {
	        for (ConsultationTimeEnum e: ConsultationTimeEnum.values()){
	            if(e.getValue()==value){
	            	return e.getCode();
	            }
	        }
	        return WORKING_DAY_BEFOR_DAWN.code;
	}

	public static  int  consultationTimeType(int  second){
		//凌晨  0到6点之间
		if (second>=0&&second<6*60*60) {
			return ConsultationTimeEnum.WORKING_DAY_BEFOR_DAWN.code;
		}
		
		//上  6到12点之间
		if (second>=6*60*60&&second<12*60*60) {
			return ConsultationTimeEnum.WORKING_DAY_MORNING.code;
		}
		
		//下午  12到18点之间
		if (second>=12*60*60&&second<18*60*60) {
			return ConsultationTimeEnum.WORKING_DAY_AFTERNOON.code;
		}
		//晚上12到18点之间
		if (second>=18*60*60&&second<24*60*60) {
			return ConsultationTimeEnum.WORKING_DAY_NIGHT.code;
		}
		return 0;
	}

}
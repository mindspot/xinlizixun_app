package com.kuangji.paopao.enums;

import java.util.ArrayList;
import java.util.List;

import com.kuangji.paopao.admin.vo.CommonVO.WorkVO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ConsultantWorkEnum {
	VOICE(0,"语音"),
	VIDEO(1,"视频" ),
	FACING_EACH_OTHER(2,"面对面"),
	SET_MEAL(6,"语/视/面");
	
    @Getter
    private Integer code;
    @Getter
    private String value;
    
    

	public static Integer getCode(String value) {
		for (ConsultantWorkEnum ele : ConsultantWorkEnum.values()) {
			if(ele.getValue().equals(value)) {
				return ele.code;
			}
		}
		return null;
	}
	
	public static String getConsultantWorkEnumValue(int code) {
		for (ConsultantWorkEnum ele : ConsultantWorkEnum.values()) {
			if(ele.getCode().equals(code)) {
				return ele.value;
			}
		}
		return null;
	}
	
	public static List<String> listConsultantWorkEnumValue() {
		List<String> list =new ArrayList<String>();
		for (ConsultantWorkEnum ele : ConsultantWorkEnum.values()) {
			list.add(ele.value);
		}
		return list;
	}
	
	public static List<WorkVO> listWorkVO() {
		List<WorkVO> list =new ArrayList<WorkVO>();
		for (ConsultantWorkEnum ele : ConsultantWorkEnum.values()) {
			WorkVO workVO =new WorkVO();
			workVO.setCode(ele.getCode());
			workVO.setVal(ele.getValue());
			list.add(workVO);
		}
		return list;
	}
}

package com.kuangji.paopao.vo;

import java.util.List;

import com.kuangji.paopao.model.DictArea;

import lombok.Data;

@Data
public class DictAreaVO extends DictArea {


	private static final long serialVersionUID = 6247933783695416167L;
	
	private List<DictAreaVO> dictAreas;
}

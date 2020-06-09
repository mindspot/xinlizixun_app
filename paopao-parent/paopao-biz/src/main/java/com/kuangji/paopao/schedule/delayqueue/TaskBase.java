package com.kuangji.paopao.schedule.delayqueue;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
public class TaskBase {
    private String identifier;
    
    private Integer  type;
    
  
    public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public TaskBase(String identifier, Integer type) {
		super();
		this.identifier = identifier;
		this.type = type;
	}


	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
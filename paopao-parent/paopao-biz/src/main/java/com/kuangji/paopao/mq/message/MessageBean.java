package com.kuangji.paopao.mq.message;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ消息内容
 * @ClassName:   MessageObj 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      mail.chenc@foxmail.com
 * @date         2019年5月14日 下午1:40:44 
 *
 */
public class MessageBean implements java.io.Serializable{
	private static final long serialVersionUID = 6326841680266088351L;
	private int event;
	private Map<String,Object> data = new HashMap<String,Object>();
	
	public MessageBean() {
		super();
	}
	public MessageBean(int event) {
		super();
		this.event = event;
	}
	public int getEvent() {
		return event;
	}
	public void setEvent(int event) {
		this.event = event;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void addData(String key, Object value){
		this.data.put(key, value);
	}
	
	public enum MQEvent {
		
		PAY_SUCC(200,"已支付");
		
		public int code;
	    public String desc;
	    MQEvent(int code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }
	}
}

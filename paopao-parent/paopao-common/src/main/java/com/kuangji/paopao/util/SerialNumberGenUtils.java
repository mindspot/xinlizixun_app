package com.kuangji.paopao.util;

import java.util.Date;

public class SerialNumberGenUtils {
	private static SnowflakeUtils idWork = new SnowflakeUtils(0, 0);
	/**
	 * 订单编号
	 * @Description: TODO
	 * @return: String      
	 */
	public static String genOrderNo(){
		return "B"+ DateUtils.formatDate(new Date(), "yyMMdd") + idWork.nextId();
	}
	
	
	
	/**
	 * 督导订单编号
	 * @Description: TODO
	 * @return: String      
	 */
	public static String genSupervisorOrderNo(){
		return "C"+ DateUtils.formatDate(new Date(), "yyMMdd") + idWork.nextId();
	}
	
}

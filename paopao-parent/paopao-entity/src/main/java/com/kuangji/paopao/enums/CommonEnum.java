package com.kuangji.paopao.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 公共类的枚举类型
 * 
 */
public enum CommonEnum {
		//首页轮播图
		INDEX_IMAGE_TYPE(1),
		
		//首页分类
	    INDEX_CLASSIFICATION_TYPE(2),
	    
	    //首页test
		INDEX_TEST_TYPE(3),
		
		//咨询师入驻模块 个人成长的标签分类
		PERSONAL_GROWTH_TYPE(101),
		
		//咨询师入驻模块 情绪的标签分类
		EMOTION_MANAGEMENT_TYPE(102),
		
		//争对的人群
		CONTENTION_TYPE(103),
		
		//咨询页面模块
		CONSULTATION_CODE(200),
		
		//咨询页面问题
		CONSULTATION_QUESTION(201),
		
		//提现问题
		CASH_WITHDRAWAL(300),
		
		//资质分类
		CERTIFICATE(400),
		
		//咨询师入驻协议 类型
		CHECK_IN_AGREEMENT_TYPE(1001),
		
		//咨询师服务须知类型
		SERVICE_INSTRUCTIONS(2001),
		
		//支付详细说明
		SERVICE_DETAILED_DESCRIPTION(2002),
		
		//公司介绍
		COMPANY_INTRODUCTION(3001),
		
		//客服电话
		CUSTOMER_SERVICE(4001),
		
		//文章分类
		CATEGORY_CLASSIFICATION_TYPE(5001),
	
		//督导服务须知类型
		SUPERVISOR_SERVICE_NOTICE_TYPE(6001),
		
		//客服服务时间
		CUSTOMER_SERVICE_PERIOD(7001),
		
		//入驻的url
		SET_IN_SERVICE_URL(8001),
		
		//服务设置的url
		SERVICE_SETTINGS_URL(9001),
		;
	
		private int val;
		
		public int getVal() {
			return val;
		}
		private CommonEnum(int val) {
			this.val = val;
		}
		
		public static boolean isInclude(int key){
	        boolean include = false;
	        for (CommonEnum e: CommonEnum.values()){
	            if(e.getVal()==key){
	                include = true;
	                break;
	            }
	        }
	        return include;
	    }
		
		public static List<Integer> inList(){
	       List<Integer> list =new ArrayList<Integer>();
	       list.add(INDEX_IMAGE_TYPE.val);
	       list.add(INDEX_CLASSIFICATION_TYPE.val);
	       list.add(INDEX_TEST_TYPE.val);
	       list.add(CONSULTATION_QUESTION.val);
	       return list;
	    }
}

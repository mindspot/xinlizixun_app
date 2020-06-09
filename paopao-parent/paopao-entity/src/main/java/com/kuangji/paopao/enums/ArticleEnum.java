package com.kuangji.paopao.enums;

/**
 * 
 * 公共类的枚举类型
 * 
 */
public enum ArticleEnum {
		//文章 类型
		ARTICLE_TYPE(0),
		//广告类型
		ADVERTISEMENT_TYPE(1);
	
		private int val;
		
		public int getVal() {
			return val;
		}
		private ArticleEnum(int val) {
			this.val = val;
		}
		
		public static boolean isInclude(int key){
	        boolean include = false;
	        for (ArticleEnum e: ArticleEnum.values()){
	            if(e.getVal()==key){
	                include = true;
	                break;
	            }
	        }
	        return include;
	    }
}

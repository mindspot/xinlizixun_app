package com.kuangji.paopao.vo;

import java.io.Serializable;

import com.kuangji.paopao.model.Article;
/**
 * Author 金威正
 * Date  2020-02-17
 */

public class ArticleVO  extends Article implements Serializable {
	
	private static final long serialVersionUID = 6101164439757111511L;
    		
	private String realName="";
	
    private String headImg="";

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
    
    
    
  
}
package com.kuangji.paopao.vo;

import lombok.Data;
/**
 * 咨询详细 页面  咨询服务商品
 * Author 金威正
 * Date  2020-02-20
 */
@Data
public class MallGoodsConsultantServiceVO {
	private long id;
    
    private String goodsName="";
    
    private int sellPrice;
    
    private int goodsClass;

    private String mode="";
    
    private int  stock;
    
    private String shareText="";
    

    public MallGoodsConsultantServiceVO(){
    
    }

   
}
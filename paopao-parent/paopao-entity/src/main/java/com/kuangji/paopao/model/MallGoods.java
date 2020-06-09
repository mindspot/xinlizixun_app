package com.kuangji.paopao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "mall_goods")
@Data
public class MallGoods implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 逻辑上店铺ID  实际上是咨询师id 或者平台以后卖的商品
     */
    @Column(name = "shop_id")
    private Integer shopId;

    /**
     * 商品类型：1=咨询服务
     */
    @Column(name = "goods_class")
    private Integer goodsClass;

    /**
     * 删除标识：0=未删除，1=已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品主图
     */
    @Column(name = "goods_main_img")
    private String goodsMainImg;

    /**
     * 售价/标准价，单位：分
     */
    @Column(name = "sell_price")
    private Integer sellPrice;

    /**
     * 划线价，单位：分
     */
    @Column(name = "line_price")
    private Integer linePrice;

    /**
     * 采购折扣
     */
    @Column(name = "cost_rate")
    private BigDecimal costRate;

    /**
     * 成本价/OTA价/进价，单位：分
     */
    @Column(name = "cost_price")
    private Integer costPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商品卖点
     */
    @Column(name = "sell_point_text")
    private String sellPointText;

    /**
     * 分享文本
     */
    @Column(name = "share_text")
    private String shareText;

    /**
     * 购买须知
     */
    @Column(name = "buy_remark")
    private String buyRemark;

    /**
     * 上下架标示：0=下架，1=上架
     */
    @Column(name = "up_down_flag")
    private Integer upDownFlag;

    /**
     * H5链接
     */
    @Column(name = "goods_link")
    private String goodsLink;

    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "expires")
    private Integer expires;
    
    /**
     * 商品详情（富文本）
     */
    @Column(name = "goods_detail")
    private String goodsDetail;


    
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", shopId=").append(shopId);
        sb.append(", goodsClass=").append(goodsClass);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsMainImg=").append(goodsMainImg);
        sb.append(", sellPrice=").append(sellPrice);
        sb.append(", linePrice=").append(linePrice);
        sb.append(", costRate=").append(costRate);
        sb.append(", costPrice=").append(costPrice);
        sb.append(", stock=").append(stock);
        sb.append(", sellPointText=").append(sellPointText);
        sb.append(", shareText=").append(shareText);
        sb.append(", buyRemark=").append(buyRemark);
        sb.append(", upDownFlag=").append(upDownFlag);
        sb.append(", goodsLink=").append(goodsLink);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", goodsDetail=").append(goodsDetail);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public MallGoods() {
		super();
	}

	public MallGoods(Integer shopId, Integer isDelete) {
	
		this.shopId = shopId;
		this.isDelete = isDelete;
	}
    
	public MallGoods(Integer shopId,Integer upDownFlag,Integer isDelete) {
		this.shopId = shopId;
		this.upDownFlag=upDownFlag;
		this.isDelete = isDelete;

	}
    
    
}
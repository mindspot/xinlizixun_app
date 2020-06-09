package com.kuangji.paopao.pay;

import lombok.Data;

//支付宝参数配置
@Data
public class AlipayConfig {

    // 商户ID
    private String appid = "";
    // 私钥
    private String rsa_private_key = "";
    // 异步回调地址
    private String notify_url;
    // 同步回调地址
    private String return_url;
    // 请求网关地址
    private String gateway_url;
    // 编码
    private String charset = "UTF-8";
    // 返回格式
    private String format = "json";
    // 支付宝公钥
    private String alipay_public_key = "";
    // RSA2
    private String signtype = "RSA2";

}

package com.kuangji.paopao.pay;

import java.io.InputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPayConfig;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="wxpay")
public class WxpayConfig  implements WXPayConfig{
	
	private String appID;
	
	private String mchID;
	
	private String key;
	
	private String wxNotifyUrl;
	
	private String  nonceStr;
	
	private String  spbillCreateIp;
	
	private String  tradeType;
	@Override
	public String getAppID() {
		// TODO Auto-generated method stub
		return appID;
	}

	@Override
	public String getMchID() {
		// TODO Auto-generated method stub
		return mchID;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}
 


	
	
}

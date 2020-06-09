package com.kuangji.paopao.config;

import java.util.Objects;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.kuangji.paopao.constant.AlipayUrlConstants;

@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class ApilayAutoConfiguration {
    private static final String ERROR_MSG_APPID_NULL = "alipay application's appid cann't be null";

    private static final String ERROR_MSG_PRIVATE_KEY_NULL = "alipay application's privateKey cann't be null";

    private static final String ERROR_MSG_ALIPAY_PUBLIC_KEY_NULL = "alipay's publicKey cann't be null";

    /**
     * 构造支付宝客户端
     *
     * @param properties
     *            支付宝配置
     * @return 支付宝客户端
     */
    @Bean
    public AlipayClient alipayClient(AlipayProperties properties) {

        String gatewayUrl = AlipayUrlConstants.gateway(properties.isDev());
        String appId = Objects.requireNonNull(properties.getAppId(), ERROR_MSG_APPID_NULL);
        String privateKey = Objects.requireNonNull(properties.getPrivateKey(), ERROR_MSG_PRIVATE_KEY_NULL);
        String format = AlipayConstants.FORMAT_JSON;
        String charset = AlipayConstants.CHARSET_UTF8;
        String alipayPublicKey = Objects.requireNonNull(properties.getAlipayPublicKey(),
                ERROR_MSG_ALIPAY_PUBLIC_KEY_NULL);
        String signType = AlipayConstants.SIGN_TYPE_RSA.equals(properties.getSignType()) ? AlipayConstants.SIGN_TYPE_RSA
                : AlipayConstants.SIGN_TYPE_RSA2;

        return new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
    }
}

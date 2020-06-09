package com.kuangji.paopao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("alipay")
public class AlipayProperties {
    /**
     * 沙箱模式标志
     */
    private boolean dev;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 签名模式
     */
    private String signType;

    /**
     * 返回沙箱模式标志
     *
     * @return 沙箱模式标志
     */
    public boolean isDev() {
        return dev;
    }

    /**
     * 设置沙箱模式标志
     *
     * @param dev
     *            沙箱模式标志
     */
    public void setDev(boolean dev) {
        this.dev = dev;
    }

    /**
     * 返回应用ID
     *
     * @return 应用ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置应用ID
     *
     * @param appId
     *            应用ID
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 返回私钥
     *
     * @return 私钥
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 设置私钥
     *
     * @param privateKey
     *            私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * 返回支付宝公钥
     *
     * @return 支付宝公钥
     */
    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    /**
     * 设置支付宝公钥
     *
     * @param alipayPublicKey
     *            支付宝公钥
     */
    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    /**
     * 返回签名模式
     *
     * @return 签名模式
     */
    public String getSignType() {
        return signType;
    }

    /**
     * 设置签名模式
     *
     * @param signType
     *            签名模式
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }
}

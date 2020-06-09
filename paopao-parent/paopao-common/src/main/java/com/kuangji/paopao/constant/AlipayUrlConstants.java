package com.kuangji.paopao.constant;

public interface AlipayUrlConstants {
    /**
     * OAUTH2地址 正式环境
     */
    String OAUTH2 = "https://openauth.alipay.com/oauth2/";

    /**
     * OAUTH2地址 沙箱模式
     */
    String OAUTH2_DEV = "https://openauth.alipaydev.com/oauth2/";

    /**
     * 网关地址 正式环境
     */
    String GATEWAY = "https://openapi.alipay.com/gateway.do";

    /**
     * 网关地址 沙箱模式
     */
    String GATEWAY_DEV = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 获取OAUTH2地址
     *
     * @param dev
     *            是否为沙箱模式
     * @return OAUTH2地址
     */
    static String oauth2(boolean dev) {
        return dev ? OAUTH2_DEV : OAUTH2;
    }

    /**
     * 获取网关地址
     *
     * @param dev
     *            是否为沙箱模式
     * @return 网关地址
     */
    static String gateway(boolean dev) {
        return dev ? GATEWAY_DEV : GATEWAY;
    }
}

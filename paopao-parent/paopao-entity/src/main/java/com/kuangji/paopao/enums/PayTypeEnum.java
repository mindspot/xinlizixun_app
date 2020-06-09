package com.kuangji.paopao.enums;

/**
 * 支付方式枚举类型
 * @author Administrator
 */
public enum PayTypeEnum {
	PAY_WX_MP(1,"微信支付"),
	PAY_ZFB_LINE(2,"支付宝支付"),
	PAY_BALANCR(3,"余额支付"),
	;

	public int code;
    public String desc;
    PayTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static boolean include(int code){
        for (PayTypeEnum e: PayTypeEnum.values()){
            if(e.code == code){
            	return true; 
            }
        }
        return false;
    }

    public static String getValue(int code) {
		for (PayTypeEnum ele : PayTypeEnum.values()) {
			if(ele.getCode()==code) {
				return ele.desc;
			}
		}
		return null;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
    
}
package com.kuangji.paopao.enums;
/**
 * 支付状态枚举类型
 * @author Administrator
 */
public enum PayStatusEnum {
	PAY_WAIT(0,"待支付"),
	PAY_SUCC(1,"已成功"),
	PAY_FAIL(2,"支付失败");
	
	public int code;
    public String desc;
    PayStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
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

	public static String getValue(int code) {
		for (PayStatusEnum ele : PayStatusEnum.values()) {
			if(ele.getCode()==code) {
				return ele.desc;
			}
		}
		return null;
	}
}

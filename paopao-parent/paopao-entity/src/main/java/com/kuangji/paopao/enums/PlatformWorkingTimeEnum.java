package com.kuangji.paopao.enums;

/**
 * Author 金威正
 * Date  2020-02-27
 */
public enum PlatformWorkingTimeEnum {
	MORNING(1,"上午"),
	AFTERNOON(2,"下午"),
	NIGHT(3,"晚上"),
	BEFORE_DAWN(4,"凌晨");
	public int code;
    public String desc;
    
    PlatformWorkingTimeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static boolean include(int code){
        for (PlatformWorkingTimeEnum e: PlatformWorkingTimeEnum.values()){
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
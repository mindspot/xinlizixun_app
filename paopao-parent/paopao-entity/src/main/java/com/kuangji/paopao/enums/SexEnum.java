package com.kuangji.paopao.enums;

/**
 * 
 * 性别类的枚举类型
 * 
 */
public enum SexEnum {
	MALE("男", 0), 
	FEMALE("女", 1),
	PRIVACY("隐私",2),
	MALE_CONSULTANT("男咨询师", 0), 
	FEMALE_CONSULTANT("女咨询师", 1);
	// 成员变量
	private String index;

	private int value;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private SexEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

	public static boolean isInclude(int key){
        boolean include = false;
        for (SexEnum e: SexEnum.values()){
            if(e.getValue()==key){
                include = true;
                break;
            }
        }
        return include;
    }
}

package com.kuangji.paopao.enums;

/**
 * 
 * 性别类的枚举类型
 * 
 */
public enum UserCardEnum {
	INSERT(0, "绑定"),
	UPDATE(1, "修改");
	// 成员变量
	private int  index;

	private String value;

	

	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	private UserCardEnum(int index, String value) {
		this.index = index;
		this.value = value;
	}



	public static boolean isInclude(int key){
        boolean include = false;
        for (UserCardEnum e: UserCardEnum.values()){
            if(e.getIndex()==key){
                include = true;
                break;
            }
        }
        return include;
    }
}

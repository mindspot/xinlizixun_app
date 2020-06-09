package com.kuangji.paopao.enums;

/**
 * 
 * userEnum
 * 
 */
public enum UserCashWithdrawalEnum {
	TO_EXAMINE(0,"审核中" ), 
	REFUSE(1,"拒接" ) ,
	ADOPT(2,"通过" )
	;
	// 成员变量
	private int index;

	private String value;

	private UserCashWithdrawalEnum(int index, String value) {
		this.index = index;
		this.value = value;
	}

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
	
	public static String getValue(int code) {
		for (UserCashWithdrawalEnum ele : UserCashWithdrawalEnum.values()) {
			if(ele.getIndex()==code) {
				return ele.getValue();
			}
		}
		return null;
	}

	public static boolean isInclude(int key){
        boolean include = false;
        for (UserCashWithdrawalEnum e: UserCashWithdrawalEnum.values()){
            if(e.getIndex()==key){
                include = true;
                break;
            }
        }
        return include;
    }
}

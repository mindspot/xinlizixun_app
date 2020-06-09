package com.kuangji.paopao.enums;

/**
 * 
 * userEnum
 * 
 */
public enum UserImageTypeEnum {
	ID_CARD(1,"身份证" ), 
	DIPLOMA(2,"学历证书" ) ,
	QUALIFICATION_CERTIFIC(3,"资质证书" ) ,
	TRAINING_CERTIFICATE(4,"培训证书" ),
	LETTER_OF_PROOF(5,"证明信件" ),
	BOOK(6,"出版书籍" );
	private int index;

	private String value;

	private UserImageTypeEnum(int index, String value) {
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
		for (UserImageTypeEnum ele : UserImageTypeEnum.values()) {
			if(ele.getIndex()==code) {
				return ele.getValue();
			}
		}
		return null;
	}

}

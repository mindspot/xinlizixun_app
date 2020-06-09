package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ConsultantExtraInfoEnum{
	DIPLOMA(2,"学历" ) ,
	
	QUALIFICATION_CERTIFIC(3,"资质" ) ,
	
	TRAINING_CERTIFICATE(4,"培训" ),
	
	LETTER_OF_PROOF(5,"督导" ),
	
	BOOK(6,"出版书籍" ),
	
	PAPER(7,"论文" ),
	
	WORKING_YEARS(8,"从业年限" );
	
    @Getter
    private Integer code;
    @Getter
    private String value;
    
   
}

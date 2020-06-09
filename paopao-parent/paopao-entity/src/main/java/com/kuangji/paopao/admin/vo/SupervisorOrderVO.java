package com.kuangji.paopao.admin.vo;

import com.kuangji.paopao.model.ConsultantSupervisorOrder;

import lombok.Data;

@Data
public class SupervisorOrderVO extends ConsultantSupervisorOrder {

	private static final long serialVersionUID = -6460806717735825147L;


	private String consultantRealName;

	
	private String supervisorRealName;
	
}

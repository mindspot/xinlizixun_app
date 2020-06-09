package com.kuangji.paopao.service;

import com.kuangji.paopao.dto.SetInDTO;
/**
 * 咨询师入驻
 */
public interface SettledInService {

    int  deleteUserImg(Integer id,Integer userId);

	void insertSetInDTO(SetInDTO setInDTO, Integer userId);

    void updateSetInDTO(SetInDTO setInDTO, Integer userId);
}

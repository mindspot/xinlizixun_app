package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.DictAreaMapper;
import com.kuangji.paopao.model.DictArea;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.service.DictAreaService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.vo.DictAreaVO;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-18
 */
@Service
public class DictAreaServiceImpl extends BaseServiceImpl<DictArea, Integer> implements   DictAreaService {
    @Autowired
    private DictAreaMapper dictAreaMapper;

    @Autowired
    private ConsultantMapper consultantMapper;
    
	@Override
	public BaseMapper<DictArea> getMapper() {
		
		return dictAreaMapper;
	}

	@Override
	public List<DictAreaVO> listDictAreaVO() {
		Example example=new Example(Consultant.class);
		example.createCriteria().andEqualTo("status", LoginEnum.LOGIN_NORMAL.getIndex());
		List<Consultant> list=consultantMapper.selectByExample(example);
		
		Set<String> provNames=new HashSet<>();
		List<DictAreaVO> dictAreaVOs=new ArrayList<DictAreaVO>();
		for (Consultant consultant : list) {
			boolean bl=provNames.add(consultant.getProvName());
			if (bl) {
				DictAreaVO dictAreaVO =new DictAreaVO();
				dictAreaVO.setAreaCode(String.valueOf(consultant.getProvCode()));
				dictAreaVO.setAreaName(String.valueOf(consultant.getProvName()));
				dictAreaVOs.add(dictAreaVO);
			}
		}
		Set<String> cityNames=new HashSet<>();
		for (DictAreaVO dictAreaVO : dictAreaVOs) {
			List<DictAreaVO> dVos=new ArrayList<DictAreaVO>();
			for (Consultant consultant : list) {
				if (dictAreaVO.getAreaCode().equals(String.valueOf(consultant.getProvCode()))) {
					boolean bl=cityNames.add(consultant.getCityName());
					if (bl) {
						DictAreaVO dVo =new DictAreaVO();
						dVo.setAreaCode(String.valueOf(consultant.getCityCode()));
						dVo.setAreaName(String.valueOf(consultant.getCityName()));
						dVos.add(dVo);
					}
				}
			}
			dictAreaVO.setDictAreas(dVos);;
		}
		
		
		return dictAreaVOs;
	}
    
   
}

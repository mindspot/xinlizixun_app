package com.kuangji.paopao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.VisitorParam;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.VisitorMapper;
import com.kuangji.paopao.model.Visitor;
import com.kuangji.paopao.service.VisitorService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.VisitorVO;

import tk.mybatis.mapper.entity.Example;

@Service
public class VisitorServiceImpl extends BaseServiceImpl<Visitor, Integer> implements VisitorService{

	@Autowired
	private VisitorMapper visitorMapper;
	
	
	@Autowired
	private MallTradeOrderMapper mallTradeOrderMapper;
	
	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");
	
	@Override
	public BaseMapper<Visitor> getMapper() {
		
		return visitorMapper;
	}

	@Override
	public int insertVisitor(Visitor visitor) {
		Example example =new Example(Visitor.class);
		example.createCriteria()
				.andEqualTo("shopId", visitor.getShopId())
				.andEqualTo("userId", visitor.getUserId());
		Visitor v=visitorMapper.selectOneByExample(example);
		if (v==null) {
			return  visitorMapper.insertSelective(visitor);
		}
		visitor.setId(v.getId());
		visitor.setUpdateTime(new Date());
		return visitorMapper.updateByPrimaryKeySelective(visitor);
	}

	@Override
	public List<VisitorVO> listVisitorVO(VisitorParam param) {

        PageHelper.startPage(param.getPageNo(), param.getPageSize());
		List<VisitorVO> lVos=visitorMapper.listVisitorVO(param);
		for (VisitorVO visitorVO : lVos) {
			visitorVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, visitorVO.getHeadImg()));
			int count=mallTradeOrderMapper.getCountOrderEndByUserId(visitorVO.getUserId());
			Integer isOrder=count<1?0:1;
			visitorVO.setIsOrder(isOrder);
		}

		return lVos;
	}

	
	
}

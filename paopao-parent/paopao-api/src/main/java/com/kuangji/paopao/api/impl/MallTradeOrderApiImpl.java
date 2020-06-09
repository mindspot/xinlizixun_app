package com.kuangji.paopao.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.api.MallTradeOrderApi;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.OrderCodeEnum;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderDiagnosisUpdateMapper;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDiagnosisUpdate;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.CommonVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantOrderDiagnosisVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.MallTradeOrderVO;
import com.kuangji.paopao.vo.MemberCaseVO;

import tk.mybatis.mapper.entity.Example;

@Component
public class MallTradeOrderApiImpl implements MallTradeOrderApi {
	@Autowired
	private MallTradeOrderService mallTradeOrderService;

	@Autowired
	private MemberCaseService memberCaseService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;

	@Autowired
	private ConsultantSupervisorOrderDiagnosisUpdateMapper consultantSupervisorOrderDiagnosisUpdateMapper;

	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");
	
	// 客户订单
	@Override
	public PageInfo<MallTradeOrderVO> pageListConsultantMallTradeOrder(String token, int pageNum) {
		int userId = JwtUtils.getUserId(token);
		PageHelper.startPage(pageNum, 13);
		List<MallTradeOrderVO> list = mallTradeOrderService.listConsultantMallTradeOrder(userId);
		for (MallTradeOrderVO mallTradeOrderVO : list) {
			mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
			int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
			mallTradeOrderVO.setOrderCode(code);
			String orderTime=mallTradeOrderVO.getOrderTime();
			mallTradeOrderVO.setOrderTime(DateUtils.StringDateTime(orderTime));
		}
		PageInfo<MallTradeOrderVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	// 咨询端 可督导订单
	@Override
	public PageInfo<ConsultantWorkMemberMallTradeOrderVO> pageListConsultantWorkMemberMallTradeOrder(String token,
			int pageNum) {
		int consultantId = JwtUtils.getUserId(token);
		PageHelper.startPage(pageNum, 13);
		List<ConsultantWorkMemberMallTradeOrderVO> list = mallTradeOrderService
				.listConsultantWorkMemberMallTradeOrder(consultantId);
		for (ConsultantWorkMemberMallTradeOrderVO vo : list) {
			String ext = vo.getExt();
			JSONObject jsonObject = JSONObject.parseObject(ext);
			String date = (String) jsonObject.get(CommonConstant.CONSULTANT_WORK_DATE);
			String time = (String) jsonObject.get(CommonConstant.CONSULTANT_WORK_TIME);
			ext=DateUtils.StringDate(date)+" "+time;
			vo.setOrderTime(ext);
			vo.setExt(ext);
			vo.setHeadImg(ImageUtils.getImgagePath(imageUrl, vo.getHeadImg()));
			vo.setOrderCode(OrderCodeEnum.supervisorOrderCode(vo.getOrderStatus()));
		}
		PageInfo<ConsultantWorkMemberMallTradeOrderVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	// 咨询师正在进行中的订单
	@Override
	public PageInfo<ConsultantMallTradeOrderVO> pageListConsultantMallTradeOrderWork(String token, int pageNum) {
		int consultantId = JwtUtils.getUserId(token);
		PageHelper.startPage(pageNum, 13);
		List<ConsultantMallTradeOrderVO> list = mallTradeOrderService.listConsultantMallTradeOrderWork(consultantId);
		for (ConsultantMallTradeOrderVO mallTradeOrderVO : list) {
			mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
			int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
			mallTradeOrderVO.setOrderCode(code);
			// 转化 json
			mallTradeOrderVO.setExt(this.parseOrderExt(mallTradeOrderVO.getExt()));
			String orderTime=mallTradeOrderVO.getOrderTime();
			mallTradeOrderVO.setOrderTime(DateUtils.StringDateTime(orderTime));
		}
		mallTradeOrderService.updateOrderRead(consultantId);
		PageInfo<ConsultantMallTradeOrderVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWorkEnd(String token, int pageNum) {
		int consultantId = JwtUtils.getUserId(token);
		PageHelper.startPage(pageNum, 13);
		List<ConsultantMallTradeOrderVO> list = mallTradeOrderService.listConsultantMallTradeOrderWorkEnd(consultantId);
		for (ConsultantMallTradeOrderVO mallTradeOrderVO : list) {
			mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
			int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
			mallTradeOrderVO.setOrderCode(code);
			// 转化json 返回一个
			mallTradeOrderVO.setExt(this.parseOrderExt(mallTradeOrderVO.getExt()));
			String orderTime=mallTradeOrderVO.getOrderTime();
			mallTradeOrderVO.setOrderTime(DateUtils.StringDateTime(orderTime));
		}
		mallTradeOrderService.updateOrderRead(consultantId);
		PageInfo<ConsultantMallTradeOrderVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	/*
	 * 单个订单详情 包含病例
	 */
	@Override
	public Map<String, Object> getConsultantMallTradeOrderWork(String token, String orderNo) {
		mallTradeOrderService.unReadOrderNum(orderNo);
		Map<String, Object> map = new HashMap<String, Object>();
		Integer consultantId = JwtUtils.getUserId(token);

		ConsultantMallTradeOrderDetailsVO mallTradeOrderVO = mallTradeOrderService
				.getConsultantMallTradeOrderDetailsVO(consultantId, orderNo);
		if (mallTradeOrderVO == null) {
			return null;
		}

		mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
		int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
		mallTradeOrderVO.setOrderCode(code);
		mallTradeOrderVO.setExt(this.parseOrderExt(mallTradeOrderVO.getExt()));
		String orderTime=mallTradeOrderVO.getOrderTime();
		mallTradeOrderVO.setOrderTime(DateUtils.StringDateTime(orderTime));
		map.put(CommonConstant.ORDER_INFO, mallTradeOrderVO);
		Integer id = mallTradeOrderVO.getMemberCaseId();
		MemberCaseVO memberCaseVO = new MemberCaseVO();
		MemberCase memberCase = memberCaseService.get(id);
		BeanUtils.copyProperties(memberCase, memberCaseVO);
		String types = memberCase.getConsultantType();
		if (StringUtils.isNotBlank(types)) {
			JSONArray jsonArray = JSONObject.parseArray(types);
			Integer[] ids = new Integer[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				ids[i] = (Integer) jsonArray.get(i);
			}

			List<Common> commons = commonService.listCommon(ids);
			List<CommonVO> lVos = new ArrayList<CommonVO>();
			for (int i = 0; i < commons.size(); i++) {
				CommonVO commonVO = new CommonVO();
				commonVO.setVal(commons.get(i).getVal());
				lVos.add(commonVO);
			}
			memberCaseVO.setCommonVOs(lVos);
		}
		User user = userService.get(consultantId);
		memberCaseVO.setConsultatName(user.getRealName());

		Example example = new Example(ConsultantSupervisorOrderDiagnosisUpdate.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", mallTradeOrderVO.getOrderNo());
		List<ConsultantOrderDiagnosisVO> diagnosisVOs = mallTradeOrderService.listConsultantMallTradeDiagnosis(orderNo,consultantId);

		List<ConsultantOrderDiagnosisVO> imgs = diagnosisVOs.stream().filter(c -> c.getContentType() == 1)
				.collect(Collectors.toList());
		
		for (ConsultantOrderDiagnosisVO c : imgs) {
			c.setContent(ImageUtils.getImgagePath(imageUrl, c.getContent())+CommonConstant.IMG_SIZE);
			c .setImgeSize(CommonConstant.IMG_SIZE);
		}
		memberCaseVO.setConsultantOrderDiagnosisVOs(imgs);
		map.put(CommonConstant.MEMBER_CASE, memberCaseVO);
		
		long count = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).count();
		if (count != 0) {
			ConsultantOrderDiagnosisVO vo = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).findFirst().get();
			vo.setImgeSize("");
			memberCaseVO.setConsultantOrderDiagnosisVO(vo);
		}
		ConsultantSupervisorOrderDiagnosisUpdate update = consultantSupervisorOrderDiagnosisUpdateMapper
				.selectOneByExample(example);
		if (update == null) {
			map.put(CommonConstant.ISDIAGNOSIS, false);
			return map;
		}
		map.put(CommonConstant.ISDIAGNOSIS, true);
		String strDateFormat = "yyyy-MM-dd";
		String date = DateUtils.formatDate(update.getDiagnosisTime(), strDateFormat);
		memberCaseVO.setConsultationTime(date);
		
		return map;
	}

	/*
	 * 多个订单详情 包含病例
	 */
	@Override
	public List<Map<String, Object>> listConsultantMallTradeOrderWork(String token, Integer memberId) {
		List<Map<String, Object>> lMaps = new ArrayList<>();
		Integer consultantId = JwtUtils.getUserId(token);
		List<ConsultantMallTradeOrderDetailsVO> list = mallTradeOrderService.listConsultantMallTradeOrderDetailsVO(consultantId, memberId);
		if (list == null) {
			return null;
		}
		for (ConsultantMallTradeOrderDetailsVO mallTradeOrderVO : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
			int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
			mallTradeOrderVO.setOrderCode(code);
			String orderTime=mallTradeOrderVO.getOrderTime();
			mallTradeOrderVO.setOrderTime(DateUtils.StringDateTime(orderTime));
			JSONObject jsonObject = JSONObject.parseObject(mallTradeOrderVO.getExt());
			String consultantWorkDate = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
			String consultantWorkTime = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
			String ext = consultantWorkDate + " " + consultantWorkTime;
			mallTradeOrderVO.setExt(ext);
			map.put(CommonConstant.ORDER_INFO, mallTradeOrderVO);

			Integer id = mallTradeOrderVO.getMemberCaseId();
			MemberCaseVO memberCaseVO = new MemberCaseVO();
			MemberCase memberCase = memberCaseService.get(id);
			BeanUtils.copyProperties(memberCase, memberCaseVO);
			String types = memberCase.getConsultantType();
			if (StringUtils.isNotBlank(types)) {
				JSONArray jsonArray = JSONObject.parseArray(types);
				Integer[] ids = new Integer[jsonArray.size()];
				for (int i = 0; i < jsonArray.size(); i++) {
					ids[i] = (Integer) jsonArray.get(i);
				}

				List<Common> commons = commonService.listCommon(ids);
				List<CommonVO> lVos = new ArrayList<CommonVO>();
				for (int i = 0; i < commons.size(); i++) {
					CommonVO commonVO = new CommonVO();
					commonVO.setVal(commons.get(i).getVal());
					lVos.add(commonVO);
				}
				memberCaseVO.setCommonVOs(lVos);
			}
			User user = userService.get(consultantId);
			memberCaseVO.setConsultatName(user.getRealName());
			Example example = new Example(ConsultantSupervisorOrderDiagnosisUpdate.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("orderNo", mallTradeOrderVO.getOrderNo());
			ConsultantSupervisorOrderDiagnosisUpdate update = consultantSupervisorOrderDiagnosisUpdateMapper
					.selectOneByExample(example);
			List<ConsultantOrderDiagnosisVO> diagnosisVOs = mallTradeOrderService
					.listConsultantMallTradeDiagnosis(mallTradeOrderVO.getOrderNo(), consultantId);
			map.put(CommonConstant.MEMBER_CASE, memberCaseVO);
			List<ConsultantOrderDiagnosisVO> imgs = diagnosisVOs.stream().filter(c -> c.getContentType() == 1)
					.collect(Collectors.toList());
			for (ConsultantOrderDiagnosisVO c : imgs) {
				c.setContent(ImageUtils.getImgagePath(imageUrl, c.getContent())+CommonConstant.IMG_SIZE);
				c.setImgeSize(CommonConstant.IMG_SIZE);
			}
			
			memberCaseVO.setConsultantOrderDiagnosisVOs(imgs);
			long count = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).count();
			if (count != 0) {
				ConsultantOrderDiagnosisVO vo = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).findFirst().get();
				vo.setImgeSize("");
				memberCaseVO.setConsultantOrderDiagnosisVO(vo);
			}

			if (update == null) {
				map.put(CommonConstant.ISDIAGNOSIS, false);
				lMaps.add(map);
				continue;
			}
			map.put(CommonConstant.ISDIAGNOSIS, true);
			String strDateFormat = "yyyy-MM-dd";
			String date = DateUtils.formatDate(update.getDiagnosisTime(), strDateFormat);
			memberCaseVO.setConsultationTime(date);
			lMaps.add(map);
		}

		return lMaps;
	}

	protected String parseOrderExt(String ext) {

		JSONObject jsonObject = JSONObject.parseObject(ext);
		String consultantWorkDate = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String consultantWorkTime = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		return DateUtils.StringDate(consultantWorkDate) + " " + consultantWorkTime;

	}

}

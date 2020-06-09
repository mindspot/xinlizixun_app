package com.kuangji.paopao.api.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.api.ConsultationApi;
import com.kuangji.paopao.base.KV;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.AgeGroupEnum;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.enums.ConsultationTimeEnum;
import com.kuangji.paopao.enums.ConsultedEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.enums.SexEnum;
import com.kuangji.paopao.enums.UserLabelEnum;
import com.kuangji.paopao.mapper.CertificationMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleRestStatusMapper;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.ConsultantScheduleRestStatus;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserLabel;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.DictAreaService;
import com.kuangji.paopao.service.MallGoodsService;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.service.UserCollectionService;
import com.kuangji.paopao.service.UserLabelService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.AboutConsultantVO;
import com.kuangji.paopao.vo.BookingTimeVO;
import com.kuangji.paopao.vo.ConsultantUpdateVO;
import com.kuangji.paopao.vo.DictAreaVO;
import com.kuangji.paopao.vo.IndividualConsultantVO;
import com.kuangji.paopao.vo.MallGoodsConsultantServiceVO;
import com.kuangji.paopao.vo.PriceVO;
import com.kuangji.paopao.vo.QualificationsVO;
import com.kuangji.paopao.vo.WorkVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component
public class ConsultationApiImpl implements ConsultationApi {

	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MallGoodsService mallGoodsService;

	@Autowired
	private UserCollectionService userCollectionService;

	@Autowired
	private MemberCaseService userCaseService;

	@Autowired
	private UserLabelService userLabelService;
	
	@Autowired
	private ConsultantService consultantService;
	
	
	@Autowired
	private ConsultantScheduleRestStatusMapper restStatusMapper;
	

	@Autowired
	private CertificationMapper  certificationMapper;
	
	@Autowired
	private ConsultationSetMealOrderService  consultationSetMealOrderService;
	
	@Autowired
	private DictAreaService   dictAreaService;

	
	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");
	   
	@Override
	public Map<String, Object> listConsultationLabel(String token) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>(64);

		Map<String, List<Common>> commons = commonService.listConsultationCommon();
		// 咨询页面问题
		map.put(CommonConstant.CONSULTATION_QUESTION, commons.get(CommonConstant.CONSULTATION_QUESTION));

		// 获取分类
		map.put(CommonConstant.ClASSIFICATION, commons.get(CommonConstant.ClASSIFICATION));
		// 获取时间
		map.put(CommonConstant.CONSULTATION_TIME, ConsultationTimeEnum.listConsultationTimeEnum());

		// 综合
		Map<String, Object> comprehensives = new HashMap<String, Object>(128);

		KV[] sexKVs = { new KV(SexEnum.MALE_CONSULTANT.getIndex(), SexEnum.MALE_CONSULTANT.getValue()),
				new KV(SexEnum.FEMALE_CONSULTANT.getIndex(), SexEnum.FEMALE_CONSULTANT.getValue()), };
		comprehensives.put(CommonConstant.CONSULTATION_SEX, sexKVs);

		KV[] ageKVs = {
				new KV(AgeGroupEnum.SIXTY.getIndex(), AgeGroupEnum.SIXTY.getValue()),
				new KV(AgeGroupEnum.SEVENTY.getIndex(), AgeGroupEnum.SEVENTY.getValue()),
				new KV(AgeGroupEnum.EIGHTY.getIndex(), AgeGroupEnum.EIGHTY.getValue()),
				new KV(AgeGroupEnum.NINETY.getIndex(), AgeGroupEnum.NINETY.getValue()),
		};
		comprehensives.put(CommonConstant.CONSULTATION_SEX, sexKVs);
		comprehensives.put(CommonConstant.CONSULTATION_AGE, ageKVs);

		PriceVO priceVO = userService.getPriceVO();
		comprehensives.put(CommonConstant.CONSULTATION_MIN_PRICE, priceVO.getMinPrice());
		comprehensives.put(CommonConstant.CONSULTATION_MAX_PRICE, priceVO.getMaxPrice());
		map.put(CommonConstant.COMPREHENSIVE, comprehensives);
		
		//城市
		List<DictAreaVO> dictAreaVOs=dictAreaService.listDictAreaVO();
		map.put(CommonConstant.DICT_CITY, dictAreaVOs);
		
		return map;
	}

	// 咨询师
	@Override
	public Map<String, Object> getConsultation(Integer userId, Integer shopId,Integer serviceCode) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>(128);
		
		AboutConsultantVO aboutConsultantVO = userService.getConsultant(shopId);

		List<WorkVO> workVOs = aboutConsultantVO.getWorkVOs();
		// 取出领域擅长标签
		List<WorkVO> beGoodAt = workVOs.stream()
				.filter(workVO -> workVO.getUserLabelType() == UserLabelEnum.DOMAIN_LABEL_TYPE.getValue())
				.collect(Collectors.toList());
		
		// 取出咨询师工作情况  排序
		List<MallGoodsConsultantServiceVO> workingModeList = mallGoodsService.listConsultantService(shopId);
		workingModeList.sort(Comparator.comparing(MallGoodsConsultantServiceVO::getGoodsClass));
		map.put(CommonConstant.WORKINGMODE, workingModeList);
		
		
		boolean bl=consultationSetMealOrderService.isPaySetMeal(shopId, userId);
		int result=bl ? 1 :0; 
		map.put(CommonConstant.PAY_SET_MEAL, result);
		
		// 取出咨询师 名称 头像 寄语
		IndividualConsultantVO individualConsultantVO = new IndividualConsultantVO();
		aboutConsultantVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, aboutConsultantVO.getHeadImg()));
		BeanUtils.copyProperties(aboutConsultantVO, individualConsultantVO);
		map.put(CommonConstant.PERSONA_PROFILE, individualConsultantVO);
		

		// 个人简介下的个人介绍
		Map<String, Object> aboutConsultants = new HashMap<String, Object>(128);
		aboutConsultants.put(CommonConstant.PERSONAL_INTRODUCTION, aboutConsultantVO.getContent());

		// 个人简介下的个人 资质 
		//需要拆表修改
		List<QualificationsVO> qualifications =certificationMapper.listQualifications(shopId);
		qualifications.forEach(c->c.setImgUrl(ImageUtils.getImgagePath(imageUrl, c.getImgUrl())));
		aboutConsultants.put(CommonConstant.QUALIFICATIONS, qualifications);

		aboutConsultants.put(CommonConstant.BE_GOOD_AT, beGoodAt);
		// 咨询师服务须知
		Article serviceInstructions = articleService.getServiceInstructions(serviceCode);
		aboutConsultants.put(CommonConstant.SERVICE_INSTRUCTIONS, serviceInstructions);

		map.put(CommonConstant.ABOUT_CONSULTAN, aboutConsultants);

		// 收藏状态 true 收藏 false 没收藏
		boolean isCollected = userCollectionService.isCollected(userId, shopId);

		map.put(CommonConstant.IS_COLLECTED, isCollected);

		return map;
	}

	@Override
	public Map<String, Object> getConsultationMaterials(String token) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>(128);

		Map<String, List<Common>> commons = commonService.listConsultationCommon();

		KV[] sexKVs = { new KV(SexEnum.MALE.getIndex(), SexEnum.MALE.getValue()),
				new KV(SexEnum.FEMALE.getIndex(), SexEnum.FEMALE.getValue()), };
		KV[] consulteds = { new KV(ConsultedEnum.CONSULTED_YES.getIndex(), ConsultedEnum.CONSULTED_YES.getValue()),
				new KV(ConsultedEnum.CONSULTED_NO.getIndex(), ConsultedEnum.CONSULTED_NO.getValue()), };
		// 获取分类
		map.put(CommonConstant.ClASSIFICATION, commons.get(CommonConstant.ClASSIFICATION));
		// 性别
		map.put(CommonConstant.CONSULTATION_SEX, sexKVs);
		// 是否接受过心理咨询
		map.put(CommonConstant.CONSULTED, consulteds);

		int userId = JwtUtils.getUserId(token);

		MemberCase userCase = userCaseService.getLastMemberCase(userId);
		// last case
		map.put(CommonConstant.LAST_CASE, userCase);
		return map;
	}

	@Override
	public Map<String, Object> getPayTypeDetailedDescription(String token) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>(64);

		KV[] payTypes = { new KV(PayTypeEnum.PAY_WX_MP.getDesc(), PayTypeEnum.PAY_WX_MP.getCode()),
				new KV(PayTypeEnum.PAY_ZFB_LINE.getDesc(), PayTypeEnum.PAY_ZFB_LINE.getCode()), };
		// 支付方式
		map.put(CommonConstant.PAY_TYPES, payTypes);

		// 详细说明
		Article detailedDescription = articleService.getServiceDetailedDescription();
		map.put(CommonConstant.DETAILED_DESCRIPTION, detailedDescription);
		Integer userId=JwtUtils.getUserId(token);
		User user=userService.getUserforUpdate(userId);
		map.put(CommonConstant.ACCOUNT, user.getAccount());		
		return map;
	}

	@Override
	public Map<String, Object> getConsultant(String token) {
		Map<String, Object> map = new HashMap<>(16);
		Integer consultantId = JwtUtils.getUserId(token);
		Example example = new Example(UserLabel.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", consultantId);
		List<UserLabel> labels = userLabelService.findByExample(example);
		// 分类
		List<Integer> classificationIds = labels.stream().filter(c -> c.getUserLabelType() == CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal())
				.map(c -> c.getLabelId()).collect(Collectors.toList());
		// 正对人群
		List<Integer> contentionIds = labels.stream().filter(c -> c.getUserLabelType() == CommonEnum.CONTENTION_TYPE.getVal()).map(c -> c.getLabelId())
				.collect(Collectors.toList());

		// 商品 服务 去重
		List<MallGoods> mallGoods = mallGoodsService.find(new MallGoods(consultantId, 0));

		List<String> setList = mallGoods.stream().map(c -> c.getSellPointText()).distinct()
				.collect(Collectors.toList());

		// 取出头像 账户名称登
		ConsultantUpdateVO consultantUpdateVO = consultantService.getConsultantUpdateVO(consultantId);

		if (classificationIds != null && !classificationIds.isEmpty()) {
			List<String> classification = commonService.listCommonName(classificationIds);
			map.put(CommonConstant.ClASSIFICATION, classification);
		}

		if (contentionIds != null && !contentionIds.isEmpty()) {
			List<String> contention = commonService.listCommonName(contentionIds);
			map.put(CommonConstant.CONTENTION, contention);
		}

		map.put(CommonConstant.WORK_TYPE, setList);
		map.put(CommonConstant.CONSULTATIONS, consultantUpdateVO);
		
		Example resExample =new Example(ConsultantScheduleRestStatus.class);
		Criteria  resCriteria=resExample.createCriteria();
		resCriteria.andEqualTo("consultantId", consultantId);
		resCriteria.andEqualTo("restDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		int count=restStatusMapper.selectCountByExample(resExample);
		map.put(CommonConstant.STOP_BOOKING, count>=1);
		return map;
	}
	
	protected Map<String, BookingTimeVO> getBookingTimeVO(List<BookingTimeVO> listBooking) {
		Map<String, BookingTimeVO> bMap=new LinkedHashMap<>();
		for (BookingTimeVO bookingTimeVO : listBooking) {
			bMap.put(bookingTimeVO.getConsultantWorkStartTime(), bookingTimeVO);
		}
		return bMap;
	}
}

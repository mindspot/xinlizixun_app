package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.admin.vo.CommonVO;
import com.kuangji.paopao.admin.vo.CommonVO.CommonLable;
import com.kuangji.paopao.admin.vo.CommonVO.WorkVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.param.ArticleCategoryParam;
import com.kuangji.paopao.dto.param.CommonParam;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.enums.CustomerServiceEnum;
import com.kuangji.paopao.enums.EducationEnum;
import com.kuangji.paopao.mapper.ArticleMapper;
import com.kuangji.paopao.mapper.CommonMapper;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.PlatformWorkModeMapper;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.PlatformWorkingTimeService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.BookingTimeVO;
import com.kuangji.paopao.vo.CustomeServiceVO;
import com.kuangji.paopao.vo.PlatformWorkModeVO;
import com.kuangji.paopao.vo.SettledInConsultantVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Author 金威正 Date 2020-02-16
 */
@Service
public class CommonServiceImpl  extends BaseServiceImpl<Common, Integer> implements CommonService  {
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private PlatformWorkModeMapper workMapper;
	

	@Autowired
	private  ArticleMapper  articleMapper;
	
	@Autowired
	private ConsultantMapper consultantMapper;
	
	
	@Autowired
	private MallGoodsMapper   mallGoodsMapper;
	
	private static String IMAGE_URL= PropertiesFileUtil.getInstance().get("image_url");
	
	private static String defaultUserHeadImage= PropertiesFileUtil.getInstance().get("default_user_head_image");
	
	private static final Long platformWorkingTime = Long.valueOf(PropertiesFileUtil.getInstance().get("platform_working_time"));

	
	@Autowired
	private   PlatformWorkingTimeService  platformWorkingTimeService;

	//入驻的时候获取的标签
	@Override
	public Map<String, Object> listIndexCommon(String token) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String,Object>(128);
		
		List<Integer> types=new ArrayList<>();
		types.add(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
		types.add(CommonEnum.CONTENTION_TYPE.getVal());
		types.add(CommonEnum.CERTIFICATE.getVal());
		List<Common> list = commonMapper.listIndexCommon(types);
		// 分类
		List<Common> classificationList = list.stream()
				.filter(common -> common.getType() == CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal()).collect(Collectors.toList());
		// 争对人群
		List<Common> contentionList = list.stream()
				.filter(common -> common.getType() == CommonEnum.CONTENTION_TYPE.getVal()).collect(Collectors.toList());
		
		//证书
		List<Common> certificate = list.stream()
				.filter(common -> common.getType() == CommonEnum.CERTIFICATE.getVal()).collect(Collectors.toList());
		List<PlatformWorkModeVO> lWorks=workMapper.listWorkLable();
	
		lWorks.forEach(c->c.setWorkValName(ConsultantWorkEnum.getConsultantWorkEnumValue(c.getWorkVal())));
		Article article=articleMapper.getArticleByCode(CommonEnum.CHECK_IN_AGREEMENT_TYPE.getVal());
		map.put(CommonConstant.CHECK_IN_AGREEMENT_TYPE, article);
		map.put(CommonConstant.CONTENTION, contentionList);
		map.put(CommonConstant.ClASSIFICATION, classificationList);
		//工作模式
		map.put(CommonConstant.WORK_TYPE, ConsultantWorkEnum.listConsultantWorkEnumValue());
		
		map.put(CommonConstant.EDUCATION, EducationEnum.listEducationEnum());
		map.put(CommonConstant.CERTIFICATE, certificate);
		Integer userId =JwtUtils.getUserId(token);
		SettledInConsultantVO settledInConsultantVO=consultantMapper.getSettledInConsultantVO(userId);
		
		String headImage=settledInConsultantVO.getHeadImg();
		if (StringUtils.isBlank(headImage)) {
			headImage=defaultUserHeadImage;
		}
		settledInConsultantVO.setHeadImg(ImageUtils.getImgagePath(IMAGE_URL, headImage));
		List<UserImage> cardVOs=settledInConsultantVO.getCards();
		if (cardVOs!=null&&!cardVOs.isEmpty()) {
			cardVOs.forEach(c->c.setImgUrl(ImageUtils.getImgagePath(IMAGE_URL, c.getImgUrl())));
		}
		map.put(CommonConstant.CONSULTATIONS, settledInConsultantVO);
		
		map.put(CommonConstant.PLATFORM_WORKING_TIME, platformWorkingTime);
		
		return map;
	}

	// 获取分类
	@Override
	public List<Common> listCommonByType() {
		return commonMapper.listCommonByType(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
	}

	@Override
	public Map<String, List<Common>> listConsultationCommon() {
		
		Map<String, List<Common>> map = new HashMap<String, List<Common>>(64);
		 Example example = new Example(Common.class);
		 Criteria criteria=example.createCriteria();
		 criteria.andEqualTo("isDelete", 0);
		 criteria.andIn("type", CommonEnum.inList());
		 example.setOrderByClause("sort");
		 
		List<Common> list = commonMapper.selectByExample(example);

		// 咨询页面的问题

		List<Common> consultationQuestion = list.stream()
				.filter(common -> common.getType() == CommonEnum.CONSULTATION_QUESTION.getVal())
				.collect(Collectors.toList());

		// 分类
		List<Common> classificationList = list.stream()
				.filter(common -> common.getType() == CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal()).collect(Collectors.toList());

		map.put(CommonConstant.ClASSIFICATION, classificationList);
		
		map.put(CommonConstant.CONSULTATION_QUESTION, consultationQuestion);

		return map;
	}

	@Override
	public List<Common> listCommon(List<Integer> types) {
		// TODO Auto-generated method stub
		return commonMapper.listIndexCommon(types);
	}


	@Override
	public BaseMapper<Common> getMapper() {
		// TODO Auto-generated method stub
		return commonMapper;
	}


	@Override
	public Common getCommonByType(int type) {
		// TODO Auto-generated method stub
		return commonMapper.getCommonByType(type);
	}

	@Override
	public List<Common> listArticleCategory(ArticleCategoryParam articleCategoryParam) {
		Example example=new Example(Common.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("type", CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
		criteria.andEqualTo("isDelete",0);
		example.setOrderByClause("sort");
		PageHelper.startPage(articleCategoryParam.getPageNo(), articleCategoryParam.getPageSize());
		List<Common> commons=commonMapper.selectByExample(example);
		commons.forEach(c->c.setIcon(ImageUtils.getImgagePath(IMAGE_URL, c.getIcon())));
		return commons;
	}

	@Override
	public int addArticleCategory(Common common) {
		common.setType(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
		
		return commonMapper.insertSelective(common);
	}

	@Override
	public int updateArticleCategory(Common common) {
		String icon=common.getIcon();
    	if (StringUtils.isNotBlank(icon)&&common.getType()==CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal()) {
    		common.setIcon(icon.replace(IMAGE_URL, ""));
		}
		return commonMapper.updateByPrimaryKey(common);
	}

	@Override
	public List<Common> listCommon(Integer[] ids) {

		return commonMapper.listCommon(ids);
	}

	@Override
	public List<String> listCommonName(List<Integer> ids) {
		
		return commonMapper.listCommonName(ids);
	}

	@Override
	public List<Common> listCommon(CommonParam commonParam) {
	
		PageHelper.startPage(commonParam.getPageNo(), commonParam.getPageSize());
		Example example =new Example(Common.class);
		example.setOrderByClause("sort");
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("type", commonParam.getType());
		criteria.andEqualTo("isDelete", 0);
		List<Common> commons=commonMapper.selectByExample(example);
		if (commonParam.getType()!=null&&commonParam.getType()==CommonEnum.INDEX_IMAGE_TYPE.getVal()) {
			commons.forEach(c->c.setVal(ImageUtils.getImgagePath(IMAGE_URL, c.getVal())));
		}
		
		return commons;
	}

	@Override
	public List<Common> listCommonContent(CommonParam commonParam) {
	
		PageHelper.startPage(commonParam.getPageNo(), commonParam.getPageSize());
		Example example =new Example(Common.class);
		example.setOrderByClause("sort");
		Criteria criteria=example.createCriteria();
		List<Integer> types=new ArrayList<Integer>();
		types.add(CommonEnum.CASH_WITHDRAWAL.getVal());
		types.add(CommonEnum.CHECK_IN_AGREEMENT_TYPE.getVal());
		types.add(CommonEnum.SERVICE_INSTRUCTIONS.getVal());
		types.add(CommonEnum.COMPANY_INTRODUCTION.getVal());
		types.add(CommonEnum.SERVICE_DETAILED_DESCRIPTION.getVal());
		types.add(CommonEnum.SUPERVISOR_SERVICE_NOTICE_TYPE.getVal());
		criteria.andIn("type", types);
		criteria.andEqualTo("isDelete", 0);
		List<Common> commons=commonMapper.selectByExample(example);
		if (commonParam.getType()!=null&&commonParam.getType()==CommonEnum.INDEX_IMAGE_TYPE.getVal()) {
			commons.forEach(c->c.setVal(ImageUtils.getImgagePath(IMAGE_URL, c.getVal())));
		}
		return commons;
	}

	@Override
	public CommonVO setInLables(String token) {
		Integer userId=JwtUtils.getUserId(token);
		CommonVO commonVO =new CommonVO();
		List<Integer> types=new ArrayList<Integer>();
		types.add(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
		types.add(CommonEnum.CONTENTION_TYPE.getVal());
		List<CommonLable> commons=commonMapper.listSetInCommon(types, userId);
		
		List<CommonLable> classificationList = commons.stream()
				.filter(common -> common.getType() == CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal()).collect(Collectors.toList());
		List<CommonLable> contentions = commons.stream()
				.filter(common -> common.getType() == CommonEnum.CONTENTION_TYPE.getVal()).collect(Collectors.toList());
		
		List<BookingTimeVO> bookingTimes=platformWorkingTimeService.listWorkingUserTime(userId);
		
		List<MallGoods> mallGoods=mallGoodsMapper.select(new MallGoods(userId, 0));
		List<WorkVO> workVOs=new ArrayList<>();
		for (MallGoods goods : mallGoods) {
			WorkVO workVO=new WorkVO();
			workVO.setSellPrice(goods.getSellPrice());
			workVO.setServiceTimes(goods.getStock());
			workVO.setVal(goods.getSellPointText());
			workVO.setCode(ConsultantWorkEnum.getCode(goods.getSellPointText()));
			workVOs.add(workVO);
		}
		commonVO.setWorkVOs(workVOs);
		commonVO.setClassInfos(classificationList);
		commonVO.setContentions(contentions);
		commonVO.setBookingTimes(bookingTimes);
		return commonVO;
	}

	@Override
	public CustomeServiceVO getCustomeServiceVO() {
		CustomeServiceVO vo=new CustomeServiceVO();
		List<Common> list=commonMapper.listCommonByType(CommonEnum.CUSTOMER_SERVICE.getVal());
		
		List<Common> consultantCustomers =list.stream().filter(c->c.getValCode()==CustomerServiceEnum.CONSULTANT.getValue()).collect(Collectors.toList());
		vo.setConsultantCustomers(consultantCustomers);
		
		List<Common> consultantCustomersNow =list.stream().filter(c->c.getValCode()==CustomerServiceEnum.CONSULTANT_SERVICE_NOW.getValue()).collect(Collectors.toList());
		vo.setConsultantCustomersNow(consultantCustomersNow);
		
		List<Common> userCustomers=list.stream().filter(c->c.getValCode()==CustomerServiceEnum.CUSTOMER.getValue()).collect(Collectors.toList());
		vo.setUserCustomers(userCustomers);
		
		List<Common> userCustomersNow=list.stream().filter(c->c.getValCode()==CustomerServiceEnum.CUSTOMER_SERVICE_NOW.getValue()).collect(Collectors.toList());
		vo.setUserCustomersNow(userCustomersNow);
		
		Common common=commonMapper.getCommonByType(CommonEnum.CUSTOMER_SERVICE_PERIOD.getVal());
		vo.setCustomerServicePeriod(common);
		
		return vo;
	}

	@Override
	public List<Common> listAllArticleCategory() {
		Example example=new Example(Common.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("type", CommonEnum.CATEGORY_CLASSIFICATION_TYPE.getVal());
		criteria.andEqualTo("isDelete",0);
		example.setOrderByClause("sort");
		return commonMapper.selectByExample(example);
	}

	@Override
	public Common getCommonByVal(String commonName) {
		Example example=new Example(Common.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("type", CommonEnum.CATEGORY_CLASSIFICATION_TYPE.getVal());
		criteria.andEqualTo("val", commonName.trim());
		criteria.andEqualTo("isDelete",0);
		return commonMapper.selectOneByExample(example);
	}
}

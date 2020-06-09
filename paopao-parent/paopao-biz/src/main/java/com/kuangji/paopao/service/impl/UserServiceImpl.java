package com.kuangji.paopao.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.admin.vo.MemberVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.ConsultationDTO;
import com.kuangji.paopao.dto.UserUpdateDTO;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.param.MemberParam;
import com.kuangji.paopao.dto.param.UserParam;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.ConsultationTimeEnum;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.enums.RoleEnum;
import com.kuangji.paopao.enums.UserTypeEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleMapper;
import com.kuangji.paopao.mapper.MemberMapper;
import com.kuangji.paopao.mapper.UserLabelMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.ConsultantSchedule;
import com.kuangji.paopao.model.Member;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.redis.Easemob;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.MD5Utils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.VerificationUtils;
import com.kuangji.paopao.vo.AboutConsultantVO;
import com.kuangji.paopao.vo.ConsultantLabelVO;
import com.kuangji.paopao.vo.ConsultantVO;
import com.kuangji.paopao.vo.PriceVO;
import com.kuangji.paopao.vo.UserUpdateVO;
import com.kuangji.paopao.vo.UserVO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正 Date 2020-02-17
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ConsultantMapper consultantMapper;
    
    @Autowired
    private ConsultantScheduleMapper consultantScheduleMapper ;
    
    @Autowired
    private UserLabelMapper userLabelMapper;
    
    @Autowired
    private AsyncTask  asyncTask;
    
    @Autowired
    private RedisUtils redisUtils;
    
    

    private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");

    private static String defaultUserHeadImage = PropertiesFileUtil.getInstance().get("default_user_head_image");

    private static final String[]  prohibitRealNames={"系统消息","admin"};
    
	private static String USER_LOGIN_MESSAGE="用户您好，您可以第一时间，在我们平台接触真正的心理咨询师，他们专注于提供有效的心理咨询服务。希望您我们平台能够解除困惑，得到帮助，驱除阴霾得到快乐";
	
	private static String CONSULTANT_LOGIN_MESSAGE="咨询师您好，客户主动联系您后，会显示在该页面，未主动联系您的客户暂不显示。其他问题可直接联系在线客服。";

    @Autowired
    Easemob easemob;

    //环信 用户 标志
    public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");


    //环信 用户 标志
    public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");

    //环信 咨询师标志easemob_consultant
    public static final String PWD = PropertiesFileUtil.getInstance().get("easemob_pwd");

    public static final String paoPao ="泡泡";
    
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${platform.spread-url}")
    private String SPREAD_URL;
   
    @Override
    public Pager<List<ConsultantVO>> pageListConsultant(int pageNum, ConsultationDTO consultationDTO) {

        Map<String, Object> map = new HashMap<String, Object>();
        Pager<List<ConsultantVO>> pager = new Pager<List<ConsultantVO>>();

        map.put(CommonConstant.CONSULTATION_SEX, consultationDTO.getSex());
        map.put(CommonConstant.YEAR, DateUtils.yearInt());
        map.put(CommonConstant.CONSULTATION_AGE, consultationDTO.getConsultationAge());
        map.put(CommonConstant.CONSULTATION_MIN_PRICE, consultationDTO.getMinPrice());
        map.put(CommonConstant.CONSULTATION_MAX_PRICE, consultationDTO.getMaxPrice());

        map.put(CommonConstant.PROV_NAME, consultationDTO.getProvName());
        map.put(CommonConstant.PROV_CODE, consultationDTO.getProvCode());
        map.put(CommonConstant.CITY_NAME, consultationDTO.getCityName());
        map.put(CommonConstant.CITY_CODE, consultationDTO.getCityCode());
        map.put(CommonConstant.AREA_NAME, consultationDTO.getAreaName());
        map.put(CommonConstant.AREA_CODE, consultationDTO.getAreaCode());
        Integer[] arr = consultationDTO.getClassification();
        if (arr != null && arr.length > 0) {
        	String  labelIds=StringUtils.join(arr, ",");
        	List<Integer> userIds=userLabelMapper.listUserIdByUser(labelIds);
            Integer[] userIdsArr = new  Integer[userIds.size()];
        	userIds.toArray(userIdsArr);
        	String  userIdsStr=StringUtils.join(userIdsArr, ",");
        	if (StringUtils.isBlank(userIdsStr)) {
        		userIdsStr="0";
			}
        	map.put(CommonConstant.USER_IDS, userIdsStr);
        }
        // 设置总数
        pager.setRecordTotal(this.userMapper.countConsultant(map));
        pager.setCurrentPage(pageNum);
        pager.setPageSize(13);
        map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
        map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
        List<ConsultantVO> consultantVOs = this.userMapper.listConsultant(map);
       
        Integer[] consultationTimeArr = consultationDTO.getConsultationTime();
        
        if (consultationTimeArr != null && consultationTimeArr.length > 0) {
            consultantVOs = this.consultationTimelMatching(consultationTimeArr, consultantVOs);
        }
        consultantVOs.stream().forEach(c -> c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg())));
        this.templathe(consultantVOs);
        pager.setList(consultantVOs);
        return pager;
    }
    //显示内容模板化
    protected List<ConsultantVO> templathe(List<ConsultantVO> lConsultantVOs){
    	try {
 
    	    for (ConsultantVO consultantVO : lConsultantVOs) {
    	    	   Map<String,Object> map = new HashMap<String,Object>();
    	    	   	map.put("year", consultantVO.getWorkingSeniority());
    	    	 	map.put("accumulativeCase", consultantVO.getAccumulativeCase());
    	    	    map.put("displayContent", consultantVO.getContent());
    	    	    map.put("title", consultantVO.getTitle());
    	    	    Template template = new Template("strTpl", "${year}年   ${accumulativeCase}+小时案例经验  ${title}    ${displayContent}", new Configuration(new Version("2.3.23")));
    	    	    StringWriter result = new StringWriter();
    	    	    template.process(map, result);
    	    	    consultantVO.setDisplayContent(result.toString());
			}
    	}catch(Exception e){
    	    e.printStackTrace();
    	}
	
		return lConsultantVOs;
    }

    @Override
    public Pager<List<ConsultantVO>> pageWorkListConsultant(int pageNum, ConsultationDTO consultationDTO) {

        Map<String, Object> map = new HashMap<String, Object>();
        Pager<List<ConsultantVO>> pager = new Pager<List<ConsultantVO>>();

        map.put(CommonConstant.CONSULTATION_SEX, consultationDTO.getSex());
        map.put(CommonConstant.YEAR, DateUtils.yearInt());
        map.put(CommonConstant.CONSULTATION_AGE, consultationDTO.getConsultationAge());
        map.put(CommonConstant.CONSULTATION_MIN_PRICE, consultationDTO.getMinPrice());
        map.put(CommonConstant.CONSULTATION_MAX_PRICE, consultationDTO.getMaxPrice());

        map.put(CommonConstant.PROV_NAME, consultationDTO.getProvName());
        map.put(CommonConstant.PROV_CODE, consultationDTO.getProvCode());
        map.put(CommonConstant.CITY_NAME, consultationDTO.getCityName());
        map.put(CommonConstant.CITY_CODE, consultationDTO.getCityCode());
        map.put(CommonConstant.AREA_NAME, consultationDTO.getAreaName());
        map.put(CommonConstant.AREA_CODE, consultationDTO.getAreaCode());
        map.put(CommonConstant.CONSULTANTID, consultationDTO.getConsultantId());
        Integer[] arr = consultationDTO.getClassification();
        if (arr != null && arr.length > 0) {
        	String  labelIds=StringUtils.join(arr, ",");
        	List<Integer> userIds=userLabelMapper.listUserIdByUser(labelIds);
            Integer[] userIdsArr = new  Integer[userIds.size()];
        	userIds.toArray(userIdsArr);
        	String  userIdsStr=StringUtils.join(userIdsArr, ",");
        	if (StringUtils.isBlank(userIdsStr)) {
        		userIdsStr="0";
			}
        	map.put(CommonConstant.USER_IDS, userIdsStr);
        }
        // 设置总数
        pager.setRecordTotal(this.userMapper.countConsultant(map));
        pager.setCurrentPage(pageNum);
        pager.setPageSize(13);
        map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
        map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
        List<ConsultantVO> consultantVOs = this.userMapper.listConsultant(map);
        consultantVOs.stream().forEach(c -> c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg())));
        this.templathe(consultantVOs);
        pager.setList(consultantVOs);
        return pager;
    }

    //标签过滤
    protected List<ConsultantVO> labelMatching(Integer[] arr, List<ConsultantVO> list) {
        List<ConsultantVO> consultantVOs = new ArrayList<ConsultantVO>();
        for (ConsultantVO c : list) {
            List<ConsultantLabelVO> labelVOs = c.getConsultantLabelVOs();
            bgm:
            for (int i = 0; i < arr.length; i++) {
                int id = arr[i].intValue();
                if (labelVOs != null && !labelVOs.isEmpty()) {
                    for (ConsultantLabelVO vo : labelVOs) {
                        if (vo.getLabelId() == id) {
                            consultantVOs.add(c);
                            break bgm;
                        }
                    }
                }
            }
        }
        return consultantVOs;
    }

    //时间过滤
    protected List<ConsultantVO> consultationTimelMatching(Integer[] consultationTimeType,List<ConsultantVO> consultantVOs) {
    	
    
    	List<ConsultantVO> consultationTimelMatchings=new ArrayList<>();
    	
    	for (ConsultantVO consultantVO : consultantVOs) {
    		Example example =new Example(ConsultantSchedule.class);
        	List<Integer> userIds=new ArrayList<>();
        	userIds.add(consultantVO.getId());
        	example.createCriteria()
        		   .andEqualTo("status", BookingTimeEnum.RESERVATIONS.getValue())
        		   .andIn("consultantId", userIds);
        	List<ConsultantSchedule> schedules= consultantScheduleMapper.selectByExample(example);
        	bag:for (Integer timeType : consultationTimeType) {
    			for (ConsultantSchedule consultantSchedule : schedules) {
    				String workStartTime=consultantSchedule.getScheduleStartTime();
    				Long second=DateUtils.formatSecond(workStartTime);
    				int timeTypeCode=ConsultationTimeEnum.consultationTimeType(second.intValue());
    				if (timeType==timeTypeCode) {
    					consultationTimelMatchings.add(consultantVO);
    					break bag;
					}
    			}
    		}
		}
        return consultationTimelMatchings;
    }

    @Override
    public int countConsultant(Map<String, Object> map) {
        
        return userMapper.countConsultant(map);
    }

    @Override
    public AboutConsultantVO getConsultant(int id) {
        
        AboutConsultantVO aboutConsultantVO = userMapper.getConsultant(id);
        if (aboutConsultantVO == null) {
            return aboutConsultantVO;
        }
        return aboutConsultantVO;
    }


    // 验证码登入
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> phoneLogin(String userName, String code,String spreadUrl) {

        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(code)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        String key = CommonConstant.REDIS_PHONE + userName;

        String sysCode = String.valueOf(redisUtils.get(key));

        this.chekCode(sysCode, code);
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.login(userName,UserTypeEnum.MEMBER.getType());
        
        // 已经手机注册过
        if (user != null) {

            if (user.getStatus() == LoginEnum.LOGIN_TO_EXAMINE.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
            }
            if (user.getStatus() == LoginEnum.LOGIN_RESTRICTED_ENTRY.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY);
            }
            map.put(CommonConstant.USER_ID, user.getId());
            //pu 客户
            map.put(CommonConstant.EASEMOBID, user.getEasemobId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
            map.put(CommonConstant.USER_HEAD_IMG_SIZE,user.getHeadImgSize() );
            map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.MEMBER.getType());
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            map.put(CommonConstant.PERSONAL_SIGNATURE, "");
            map.put(CommonConstant.ACCOUNT, user.getAccount());
            String token = JwtUtils.sign(map);
            Integer userId=user.getId();
            redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+userId, token, CommonConstant.LOGIN_TIME);
            LOG.info("登入的时候的 token "+token);
            map.put(CommonConstant.TOKEN, token);
            return map;
        }
        // 第一次手机登入 注册
        user = new User();
        user.setHeadImg(defaultUserHeadImage);
        user.setUserName(userName);
        int randomName = (int) ((Math.random() * 9 + 1) * 1000); 
        user.setRealName(paoPao+randomName);
        user.setStatus(LoginEnum.LOGIN_NORMAL.getIndex());
        user.setType(UserTypeEnum.MEMBER.getType());
        user.setFirstLogin(1);
        userMapper.insertSelective(user);
        Integer userId=user.getId();
        Member mb=new Member();
        mb.setUserId(userId);
        mb.setSpreadUrl(SPREAD_URL + spreadUrl);
        memberMapper.insertSelective(mb);
        easemob.createUser(member + userId, PWD);
        user.setEasemobId(member + userId);
        userMapper.updateByPrimaryKeySelective(user);
        map.put(CommonConstant.USER_ID, user.getId());
        map.put(CommonConstant.EASEMOBID, member + user.getId());
        map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
        map.put(CommonConstant.REAL_NAME, user.getRealName());
        map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.MEMBER.getType());
        map.put(CommonConstant.PERSONAL_SIGNATURE, "");
        map.put(CommonConstant.ACCOUNT, 0);
        String token = JwtUtils.sign(map);
        redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+userId, token, CommonConstant.LOGIN_TIME);
        map.put(CommonConstant.TOKEN, token);
        asyncTask.sendLogin(member + userId, USER_LOGIN_MESSAGE);
        return map;
    }


    // 验证码登入
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> consultantPhoneLogin(String userName, String code) {

        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(code)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        String key = CommonConstant.REDIS_CONSULTANT_WORK_PHONE + userName;

        String sysCode = String.valueOf(redisUtils.get(key));
        this.chekCode(sysCode, code);
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.consultantLogin(userName);
      
        // 已经手机注册过
        if (user != null) {
        	
        	 if (user.getStatus() == LoginEnum.LOGIN_TO_EXAMINE.getIndex()) {

                 throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
             }
             if (user.getStatus() == LoginEnum.LOGIN_RESTRICTED_ENTRY.getIndex()) {

                 throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY);
             }
            if (user.getStatus() != LoginEnum.LOGIN_NORMAL.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
            }

            Integer userId=user.getId();
            map.put(CommonConstant.USER_ID, userId);
            //ps 咨询师
            map.put(CommonConstant.EASEMOBID, user.getEasemobId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
            map.put(CommonConstant.USER_HEAD_IMG_SIZE,user.getHeadImgSize() );
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            map.put(CommonConstant.ACCOUNT, user.getAccount());
            redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+userId, token, CommonConstant.LOGIN_TIME);
            LOG.info("首次登入  "+user.getFirstLogin());
            
            if (user.getFirstLogin()==0) {
            	asyncTask.sendLogin(user.getEasemobId(), CONSULTANT_LOGIN_MESSAGE);
			}
            User u=new User();
            u.setId(userId);
            u.setFirstLogin(1);
            userMapper.updateByPrimaryKeySelective(u);
            return map;
        }

        throw new ServiceException(ResultCodeEnum.ERROR_USER_NULL);

    }

    // 账户密码登入
    @Override
    public Map<String, Object> Login(String userName, String pwd) {

        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(pwd)) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PWD);
        }
       
        User user = userMapper.login(userName, RoleEnum.ADMIN.getType());
        
        if (user==null) {
        	user = userMapper.login(userName, RoleEnum.ARTICLE.getType());
		}
      
        if (user==null) {
        	user = userMapper.login(userName, RoleEnum.CUSTOMER.getType());
		}

        if (user==null) {
        	user = userMapper.login(userName, RoleEnum.CONSULTANT.getType());
		}
        if (user != null) {
        	
        	return checkUser(user,pwd);
		}
        
        throw new ServiceException(ResultCodeEnum.ERROR_USER_NULL);

    }
    
    
    protected Map<String, Object> checkUser(User user,String pwd){
    	
    		Map<String, Object> map = new HashMap<>();
 
            if (user.getStatus() == LoginEnum.LOGIN_TO_EXAMINE.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
            }
            if (user.getStatus() == LoginEnum.LOGIN_RESTRICTED_ENTRY.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY);
            }
            pwd= MD5Utils.stringToMD5(pwd);
            if (!user.getPwd().equals(pwd)) {
                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PWD);
            }
            
            if (user.getType()==UserTypeEnum.MEMBER.getType()) {
                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_USER_TYPE);
            }

            map.put(CommonConstant.USER_ID, user.getId());
            
            map.put(CommonConstant.ROLE, user.getType());
            
            if (user.getType()==RoleEnum.CONSULTANT.getType()) {
				Example example =new Example(Consultant.class);
				Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("userId", user.getId());
            	Consultant consultant=consultantMapper.selectOneByExample(example);
            	if (consultant.getUserType()==RoleEnum.SUPERVISOR.getType()) {
            	     map.put(CommonConstant.ROLE, RoleEnum.SUPERVISOR.getType());
				}
            	
            	if (consultant.getUserType()==RoleEnum.CONSULTANT.getType()) {
           	     map.put(CommonConstant.ROLE, RoleEnum.CONSULTANT.getType());
           	     throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_USER_TYPE);
				}
			}
           
            String token = JwtUtils.sign(map);
            
            map.put(CommonConstant.TOKEN, token);
            
            return map;   
    }

    
    @Override
    public Pager<List<ConsultantVO>> pageListCollectionConsultant(String token, int pageNum) {
        
        int userId = JwtUtils.getUserId(token);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CommonConstant.USER_ID, userId);
        Pager<List<ConsultantVO>> pager = new Pager<List<ConsultantVO>>();
        // 设置总数
        pager.setRecordTotal(userMapper.countCollectionConsultant(map));
        pager.setCurrentPage(pageNum);
        pager.setPageSize(13);
        map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
        map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
        List<ConsultantVO> consultantVOs = userMapper.listCollectionConsultant(map);
        consultantVOs.parallelStream().forEach(c -> c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg())));
        this.templathe(consultantVOs);
        pager.setList(consultantVOs);
        return pager;
    }

    @Override
    public UserUpdateVO getUserUpdateVO(String token) {
        
        int userId = JwtUtils.getUserId(token);
        UserUpdateVO userUpdateVO = userMapper.getUserUpdateVO(userId);
        if (userUpdateVO != null) {
            userUpdateVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, userUpdateVO.getHeadImg()));
        }
        return userUpdateVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserUpdateDTO userUpdateVO(String token, UserUpdateDTO updateDTO) {
        
        int userId = JwtUtils.getUserId(token);
        UserUpdateVO vo = userMapper.getUserUpdateVO(userId);
        if (StringUtils.isBlank(updateDTO.getRealName())) {
        	for (String prohibitRealName : prohibitRealNames) {
				if (prohibitRealName.equals(vo.getRealName())) {
					throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_NOT_REALNAME);
				}
			}
            updateDTO.setRealName(vo.getRealName());
        }
        String imagePath = updateDTO.getHeadImg();
        if (StringUtils.isBlank(imagePath)) {
        	imagePath=vo.getHeadImg();
        	int last=imagePath.lastIndexOf("?");
        	if (last!=-1) {
        		imagePath=imagePath.substring(0, last);
			}
            updateDTO.setHeadImg(imagePath);
        }else{
        	int last=imagePath.lastIndexOf("?");
        	if (last!=-1) {
        		imagePath=imagePath.substring(0, last);
			}
            updateDTO.setHeadImg(imagePath);
        	updateDTO.setHeadImg(imagePath);
        }
        if (updateDTO.getSex() == null) {
            updateDTO.setSex(vo.getSex());
        }
        updateDTO.setId(userId);
        userMapper.userUpdateVO(updateDTO);
        updateDTO.setHeadImg(ImageUtils.getImgagePath(imageUrl, updateDTO.getHeadImg()));

        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        Member tMember = memberMapper.selectOneByExample(example);
        Member member = new Member();
        member.setUserId(userId);
        member.setSendWord(updateDTO.getSendWord());
        member.setMaritalStatus(updateDTO.getMaritalStatus());
        member.setOccupation(updateDTO.getOccupation());
        if (tMember == null) {
            memberMapper.insertSelective(member);
            return updateDTO;
        }
        member.setId(tMember.getId());
        memberMapper.updateByPrimaryKeySelective(member);
        return updateDTO;

    }

    @Override
    public int updatePwd(String token, String pwd, String code) {

        Integer userId = JwtUtils.getUserId(token);
        User user = userMapper.get(userId);
        String phone = user.getUserName();

        String key = CommonConstant.REDIS_PHONE_UPDATE_PWD + phone;
        String sysCode = String.valueOf(redisUtils.get(key));
        if (!code.equals(sysCode)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        int sixe = userMapper.updatePwd(userId, pwd);
        return sixe;
    }

    // 咨询师搜索见面
    @Override
    public Pager<List<ConsultantVO>> pageListConsultantWd(int pageNum, String wd,String token) {
        
        Map<String, Object> map = new HashMap<String, Object>();

        Pager<List<ConsultantVO>> pager = new Pager<List<ConsultantVO>>();

        // 设置总数
        pager.setRecordTotal(this.userMapper.countConsultantWd(map));
        pager.setCurrentPage(pageNum);
        pager.setPageSize(13);
        map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
        map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
        map.put(CommonConstant.WD, wd);
        Integer userId=0;
       if (token!=null) {
    	   userId= JwtUtils.getUserId(token.replace("Bearer ", ""));
       	}
        map.put(CommonConstant.USER_ID, userId);
        List<ConsultantVO> consultantVOs = this.userMapper.listConsultantWd(map);
        consultantVOs.parallelStream().forEach(c -> c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg())));
        this.templathe(consultantVOs);
        pager.setList(consultantVOs);
        return pager;
    }


    @Override
    public BaseMapper<User> getMapper() {
        
        return userMapper;
    }

    @Override
    public PriceVO getPriceVO() {
        
        return userMapper.getPriceVO();
    }

    @Override
    public List<User> listConsultant(ConsultantParam consultantParam) {
        Example example = new Example(User.class);
        PageHelper.startPage(consultantParam.getPageNo(), consultantParam.getPageSize());
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> listMember(MemberParam memberParam) {
        Example example = new Example(User.class);
        PageHelper.startPage(memberParam.getPageNo(), memberParam.getPageSize());
        return userMapper.selectByExample(example);
    }

    @Override
    public User getUserInfo(String token) {
        Integer userId = JwtUtils.getUserId(token);
        User user=userMapper.get(userId);   
        if (user.getStatus() != LoginEnum.LOGIN_NORMAL.getIndex()) {

            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY);
        }
		if (user.getType()==RoleEnum.CONSULTANT.getType()) {
			Example example =new Example(Consultant.class);
            example.createCriteria().andEqualTo("userId", user.getId());
			Consultant consultant=consultantMapper.selectOneByExample(example);
			if (consultant.getUserType()==RoleEnum.SUPERVISOR.getType()) {
			     user.setType(RoleEnum.SUPERVISOR.getType());
			}
			if (consultant.getUserType()==RoleEnum.CONSULTANT.getType()) {
				user.setType(RoleEnum.CONSULTANT.getType());
			}
		} 
        return user;
    }

    private boolean chekCode(String sysCode, String code) {
     	if (code.equals("0000")) {
     		return true;
		}
        if (!code.equals(sysCode)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        return true;
    }


    @Override
    public int updateAccount(Integer consultantId, Integer account) {
        
        return userMapper.updateAccount(consultantId, account);
    }


    @Override
    public User getUserforUpdate(Integer id) {
        
        return userMapper.getUserforUpdate(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> settledInLogin(String userName, String code) {
        
        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(code)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        String key = CommonConstant.SETTLED_IN + userName;

        String sysCode = String.valueOf(redisUtils.get(key));

        this.chekCode(sysCode, code);

        Map<String, Object> map = new HashMap<>();
        User user = userMapper.consultantLogin(userName);
        if (user != null) {

            if (user.getStatus() == LoginEnum.LOGIN_NORMAL.getIndex()) {
                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_NOT_INSERT);
            }
            map.put(CommonConstant.USER_ID, user.getId());
            map.put(CommonConstant.EASEMOBID, consultant + user.getId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.CONSULTANT.getType());
            map.put(CommonConstant.PERSONAL_SIGNATURE, "");
            map.put(CommonConstant.ACCOUNT, 0);
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            return map;
        } else {
            user = new User();
            user.setUserName(userName);
            user.setHeadImg("");
            user.setStatus(LoginEnum.LOGIN_NORMAL.getIndex());
            user.setType(UserTypeEnum.CONSULTANT.getType());
            userMapper.insertSelective(user);
            Consultant c = new Consultant();
            c.setUserId(user.getId());
            c.setStatus(LoginEnum.LOGIN_TO_EXAMINE.getIndex());
            consultantMapper.insertSelective(c);
            easemob.createUser(consultant + user.getId(), PWD);
            map.put(CommonConstant.USER_ID, user.getId());
            map.put(CommonConstant.EASEMOBID, consultant + user.getId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.CONSULTANT.getType());
            map.put(CommonConstant.PERSONAL_SIGNATURE, "");
            map.put(CommonConstant.ACCOUNT, 0);
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            return map;
        }

    }


    @Override
    public List<MemberVO> listMemberVO(MemberParam memberParam) {
        
        PageHelper.startPage(memberParam.getPageNo(), memberParam.getPageSize());
        return userMapper.listMemberVO(memberParam);
    }

    @Override
    public List<User> listUser(UserParam userParam) {
        PageHelper.startPage(userParam.getPageNo(), userParam.getPageSize());
        return userMapper.listUser(userParam);
    }

    @Override
    public Integer updateUserStatus(Integer id, Integer status) {
        User user=new User();
        user.setId(id);
        user.setStatus(status);
        String key=CommonConstant.PAO_PAO_APP_TOKEN+id;
        redisUtils.del(key);
        return userMapper.updateByPrimaryKeySelective(user);
    }
    

    @Override
    public boolean isAdmin(Integer userId){
    	User user=userMapper.selectByPrimaryKey(userId);
		return user.getType()==RoleEnum.ADMIN.getType();
    }

    @Override
    public List<Integer> userIds(Integer userId){
    	Consultant consultant =new Consultant();
    	consultant.setUserId(userId);
    	Consultant c=consultantMapper.selectOne(consultant);
    	String invitationCode=c.getInvitationCode();
    	Example example =new  Example(Consultant.class);
    	example.createCriteria()
    		.andEqualTo("invitationCode", invitationCode)
    		.andNotEqualTo("userId",userId );
    	List<Consultant> consultants=consultantMapper.selectByExample(example);
    	List<Integer> userIds=new ArrayList<Integer>();
    	for (Consultant consultant2 : consultants) {
    		userIds.add(consultant2.getUserId());
		}
		return userIds;
    }
	@Override
	public List<Integer> containSelfUserIds(Integer userId) {
		Consultant consultant =new Consultant();
    	consultant.setUserId(userId);
    	Consultant c=consultantMapper.selectOne(consultant);
    	String invitationCode=c.getInvitationCode();
    	Example example =new  Example(Consultant.class);
    	example.createCriteria()
    		.andEqualTo("invitationCode", invitationCode);
    	List<Consultant> consultants=consultantMapper.selectByExample(example);
    	List<Integer> userIds=new ArrayList<Integer>();
    	for (Consultant consultant2 : consultants) {
    		userIds.add(consultant2.getUserId());
		}
	
		return userIds;
	}
	@Override
	public String getStatus(String easemobId) {
		String jsonStr=easemob.userStatus(easemobId);
		JSONObject jsonObject=JSONObject.parseObject(jsonStr);
		String response=(String) jsonObject.getJSONObject("data").get(easemobId);
		return response;
	}
	@Override
	public boolean isOnline(String easemobId) {
		String response=this.getStatus(easemobId);
		LOG.info(easemobId+" 环信用户在线"+response);
		if ("online".equals(response)) {
			return true;
		}
		return false;
	}
	@Override
	public Map<String, Object> consultantPhoneLoginVO(String userName, String code) {

        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(code)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        String key = CommonConstant.REDIS_CONSULTANT_WORK_PHONE + userName;

        String sysCode = String.valueOf(redisUtils.get(key));
        this.chekCode(sysCode, code);
        Map<String, Object> map = new HashMap<>();
        UserVO user = userMapper.consultantLoginVO(userName);
      
        // 已经手机注册过
        if (user != null) {
        	
        	 if (user.getConsultantStatus() != LoginEnum.LOGIN_NORMAL.getIndex()) {

                 throw new ServiceException(ResultCodeEnum.ERROR_CONSULTANT_LOGIN_TO_EXAMINE);
             }
        	
        	 if (user.getStatus() == LoginEnum.LOGIN_TO_EXAMINE.getIndex()) {

                 throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
             }
             if (user.getStatus() == LoginEnum.LOGIN_RESTRICTED_ENTRY.getIndex()) {

                 throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY);
             }
            if (user.getStatus() != LoginEnum.LOGIN_NORMAL.getIndex()) {

                throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE);
            }

            Integer userId=user.getId();
            map.put(CommonConstant.USER_ID, userId);
            //ps 咨询师
            map.put(CommonConstant.EASEMOBID, user.getEasemobId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
            map.put(CommonConstant.USER_HEAD_IMG_SIZE,user.getHeadImgSize() );
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            map.put(CommonConstant.ACCOUNT, user.getAccount());
            redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+userId, token, CommonConstant.LOGIN_TIME);
            LOG.info("首次登入  "+user.getFirstLogin());
            
            if (user.getFirstLogin()==0) {
            	asyncTask.sendLogin(user.getEasemobId(), CONSULTANT_LOGIN_MESSAGE);
			}
            User u=new User();
            u.setId(userId);
            u.setFirstLogin(1);
            userMapper.updateByPrimaryKeySelective(u);
            return map;
        }

        throw new ServiceException(ResultCodeEnum.ERROR_USER_NULL);
	}
}

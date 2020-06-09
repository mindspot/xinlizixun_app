package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.admin.vo.ConsultantServiceVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.ConsultantModelServiceParam;
import com.kuangji.paopao.enums.OperateTypeEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.ConsultantServiceMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.model.ConsultantModelService;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.service.ConsultantServiceService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-20
 */
@Service
public class ConsultantServiceServiceImpl extends BaseServiceImpl<ConsultantModelService, Integer> implements ConsultantServiceService {
    @Autowired
    private ConsultantServiceMapper consultantServiceMapper;

    @Autowired
    private ConsultantMapper consultantMapper;


    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    @Override
    public BaseMapper<ConsultantModelService> getMapper() {

        return consultantServiceMapper;
    }

    @Override
    public List<ConsultantServiceVO> listConsultantService(ConsultantModelServiceParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<ConsultantServiceVO> consultantServiceVOs= consultantServiceMapper.listConsultantService(param);
        return consultantServiceVOs;

    }

    @Override
    public List<ConsultantModelService> listConsultantService(Integer shopId) {
        Example example = new Example(ConsultantModelService.class);
        example.createCriteria().andEqualTo("shopId", shopId);
        return consultantServiceMapper.selectByExample(example);
    }

    private List<ConsultantModelService> getConsultantModelServiceList(List<ConsultantModelService> consultantModelServices, List<Integer> toUpdateList) {
        List<ConsultantModelService> modelServiceList = new ArrayList<>();
        for (ConsultantModelService consultantModelService : consultantModelServices) {
            for (Integer id : toUpdateList) {
                if (id.equals(consultantModelService.getGoodsId())) {
                    modelServiceList.add(consultantModelService);
                }
            }
        }
        return modelServiceList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addConsultantService(Integer shopId) {
        Example example = new Example(ConsultantModelService.class);
        example.createCriteria().andEqualTo("shopId", shopId);
        List<ConsultantModelService> consultantModelServices = consultantServiceMapper.selectByExample(example);
        for (ConsultantModelService consultantModelService : consultantModelServices) {
            MallGoods mallGoods = new MallGoods();
            BeanUtils.copyProperties(consultantModelService, mallGoods);
            mallGoods.setUpDownFlag(1);
            switch (OperateTypeEnum.getOperateTypeEnum(consultantModelService.getOperateType())){
                case ADD:
                	mallGoods.setId(null);
                    mallGoodsMapper.insertSelective(mallGoods);
                    break;
                case DELETE:
                    mallGoodsMapper.logicDeleteById(consultantModelService.getGoodsId());
                    break;
                case UPDATE:
                    mallGoods.setId(consultantModelService.getGoodsId());
                    mallGoodsMapper.updateByPrimaryKeySelective(mallGoods);
                    break;
            }
        }
        //修改单次服务最低价格
        int sellPrice = mallGoodsMapper.minSellPriceOne(shopId);
        Example consultantExample = new Example(Consultant.class);
        consultantExample.createCriteria().andEqualTo("userId", shopId);
        Consultant consultant = new Consultant();
        consultant.setConsultationFee(sellPrice);
        consultantMapper.updateByExampleSelective(consultant, consultantExample);

        //删除审核商品
        return this.deleteConsultantService(shopId);
    }

    @Override
    public int deleteConsultantService(Integer shopId) {
        Example example = new Example(ConsultantModelService.class);
        example.createCriteria().andEqualTo("shopId", shopId);
        return consultantServiceMapper.deleteByExample(example);
    }

    @Override
    public int countConsultantService(Integer shopId) {
        Example example = new Example(ConsultantModelService.class);
        example.createCriteria().andEqualTo("shopId", shopId);
        return consultantServiceMapper.selectCountByExample(example);
    }
}

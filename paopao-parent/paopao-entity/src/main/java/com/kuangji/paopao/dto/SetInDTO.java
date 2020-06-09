package com.kuangji.paopao.dto;

import java.io.Serializable;
import java.util.List;

import com.kuangji.paopao.vo.PlatformWorkingTimeVO;
import lombok.Data;

/**
 * 咨询师入驻的时候
 * Author 金威正
 * Date  2020-02-20
 */
@Data
public class SetInDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    //工作模式 
    List<WorkDTO> works;

    //工作模式
    List<WorkDTO> removeWorks;
    //领域
    Integer[]  classification;
    
    //针对人群
    Integer[]  contention;
    
    UserScheduleStatusDTO userScheduleStatusDTO;

    List<PlatformWorkingTimeVO> platformWorkingTimeVOList;

    
}
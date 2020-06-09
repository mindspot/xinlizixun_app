package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.Certification;
import com.kuangji.paopao.vo.QualificationsVO;

/**
* Created by Mybatis Generator 2020/04/07
*/
public interface CertificationMapper extends BaseMapper<Certification> {
    List<Certification> listCertification(Integer id);
    
    
    
    List<QualificationsVO>listQualifications(Integer userId); 
}
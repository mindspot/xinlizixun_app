package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.PublishMaterial;

import java.util.List;

/**
* Created by Mybatis Generator 2020/04/05
*/
public interface PublishMaterialMapper extends BaseMapper<PublishMaterial> {
    List<PublishMaterial> listPublishBook(Integer consultantId);
    List<PublishMaterial> listPublishPaper(Integer consultantId);
}
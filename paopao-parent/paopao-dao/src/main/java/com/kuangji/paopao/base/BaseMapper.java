package com.kuangji.paopao.base;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;

public interface BaseMapper<T> extends Mapper<T> {

	/**
	 * 根据id获取数据
	 * Auhor 金威正
	 * Dae  2020-02-18
	 */
     T get(Integer id);

	/**
	 * 根据传入添加查询
	 * Auhor 金威正
	 * Dae  2020-02-18
	 */
     List<T> list(T t );

	/**
	 * 查询所有
	 * Auhor 金威正
	 * Dae  2020-02-18
	 */
     List<T> listAll();

	/**
	 * 批量插入
	 * Auhor 金威正
	 * Dae  2020-02-18
	 */
     Integer insertBatch(List<T> list);

    /**
	 * 逻辑删除
	 * Auhor 金威正
	 * Dae  2019-12-12
	 */
	 Integer logicDeleteById(Integer id);
	

	
}

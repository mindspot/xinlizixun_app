package com.kuangji.paopao.service.base.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.service.base.BaseService;

import tk.mybatis.mapper.entity.Example;


public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
	public abstract BaseMapper<T> getMapper();

	@Override
	@Transactional(rollbackFor = Exception.class) // 事务回滚
	public Integer add(T t) {
		return getMapper().insertSelective(t); // 封装单表操作方法
	}

	@Override
	@Transactional
	public Integer delete(T t) {
		return getMapper().delete(t);
	}

	@Override
	public Integer deleteByExample(Example example) {
		return getMapper().deleteByExample(example);
	}

	@Override
	@Transactional
	public Integer deleteById(ID id) {
		return getMapper().deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateByPrimaryKeySelective(T t) {
		return getMapper().updateByPrimaryKeySelective(t);
	}

	@Override
	public T findOne(T t) {
		return getMapper().selectOne(t);
	}

	@Override
	public List<T> findAll() {
		return getMapper().selectAll();
	}

	@Override
	public T findById(ID id) {
		return getMapper().selectByPrimaryKey(id);
	}

	@Override
	public List<T> find(T t) {
		return getMapper().select(t);
	}

	@Override
	public List<T> findByExample(Example example) {
		return getMapper().selectByExample(example);
	}

	@Override
	public Integer updateByExampleSelective(T record, Example query) {
		return getMapper().updateByExampleSelective(record, query);
	}

	@Override
	public Integer findCount(T record) {
		return getMapper().selectCount(record);
	}

	@Override
	public Integer findCountByExample(Example query) {
		return getMapper().selectCountByExample(query);
	}

	/**
	 * 根据传入添加查询 Author 金威正 Date 2020-02-20
	 */
	public T get(Integer id) {

		return getMapper().get(id);
	}

	/**
	 * 根据传入添加查询 Author 金威正 Date 2020-02-20
	 */
	public List<T> list(T t) {

		return getMapper().list(t);
	}

	/**
	 * 查询所有 Author 金威正 Date 2020-02-20
	 */
	public List<T> listAll() {
		return getMapper().listAll();
	}

	/**
	 * 插入 Author 金威正 Date 2020-02-20
	 */
	public Integer insert(T t) {
		return getMapper().insert(t);
	}

	/**
	 * 批量插入 Author 金威正 Date 2020-02-20
	 */
	public Integer insertBatch(List<T> ts) {

		return getMapper().insertBatch(ts);
	}


	/**
	 * 逻辑删除 Author 金威正 Date 2019-12-12
	 */
	public Integer logicDeleteById(Integer id) {

		return getMapper().logicDeleteById(id);
	}

}

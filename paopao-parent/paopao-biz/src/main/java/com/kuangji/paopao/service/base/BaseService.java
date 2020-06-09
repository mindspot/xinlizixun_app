package com.kuangji.paopao.service.base;

import java.util.List;

import tk.mybatis.mapper.entity.Example;


public interface BaseService<T, ID> {
    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param t
     * @return
     */
    Integer add(T t);

    /**
     * 保存一个list实体，null的属性不会保存，会使用数据库默认值
     *
     * @param list
     * @return
     */
//    Integer batchAdd(List<T> list);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    Integer deleteById(ID id);

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param t
     * @return
     */
    Integer delete(T t);


    Integer deleteByExample(Example example);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param t
     * @return
     */
    Integer updateByPrimaryKeySelective(T t);

//    /**
//     * 根据主键更新属性不为null的值
//     *
//     * @param list
//     * @return
//     */
//    Integer batchUpdateByPrimaryKey(List<T> list);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * 查询全部结果
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param t
     * @return
     */
    List<T> find(T t);

//    /**
//     * 根据实体中的属性值进行分页查询，查询条件使用等号
//     *
//     * @param t
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    PageInfo<T> findPage(T t, Integer pageNum, Integer pageSize);

    List<T> findByExample(Example example);

    /**
     * 根据query条件更新record数据
     *
     * @param record 要更新的数据
     * @param query  查询条件
     * @return
     */
    Integer updateByExampleSelective(T record, Example query);

    /**
     * 查询数量
     *
     * @param record
     * @return
     */
    Integer findCount(T record);

    /**
     * 查询数量
     *
     * @param query
     * @return
     */
    Integer findCountByExample(Example query);
    
    
    

	/**
	 * 根据id获取数据
	 * Author 金威正
	 * Date  2020-02-20
	 */
     T get(ID id);

	/**
	 * 根据传入添加查询
	 * Author 金威正
	 * Date  2020-02-20
	 */
     List<T> list(T t);

	/**
	 * 查询所有
	 * Author 金威正
	 * Date  2020-02-20
	 */
     List<T> listAll();

	/**
	 * 插入
	 * Author 金威正
	 * Date  2020-02-20
	 */
     Integer insert(T t);

	/**
	 * 批量插入
	 * Author 金威正
	 * Date  2020-02-20
	 */
     Integer insertBatch(List<T> ts);

	   
    /**
	 * 逻辑删除
	 * Author 金威正
	 * Date  2019-12-12
	 */
     Integer logicDeleteById(ID id);


	
}

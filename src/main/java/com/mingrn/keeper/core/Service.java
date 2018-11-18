package com.mingrn.keeper.core;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.io.Serializable;
import java.util.List;

/**
 * Service 层 基础接口,其他Service 接口 请继承该接口
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public interface Service<T, DTO extends T, PK extends Serializable> {
	/**
	 * 持久化
	 *
	 * @param entity
	 */
	void insert(T entity);

	/**
	 * 批量持久化
	 * 注意:需要手动填写ID
	 *
	 * @param entities
	 */
	void batchInsert(List<T> entities);

	/**
	 * 根据id获取详情
	 *
	 * @param id
	 * @return
	 */

	T get(PK id);


	/**
	 * 获取所有
	 *
	 * @return
	 */
	List<T> find();

	/**
	 * 分页查询并排序
	 *
	 * @param orderBy
	 * @return
	 */
	List<T> find(OrderBy orderBy);

	/**
	 * 分页查询
	 *
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	PageInfo<T> find(int pageSize, int pageNumber);

	/**
	 * 分页查询并排序
	 *
	 * @param orderBy
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	PageInfo<T> find(OrderBy orderBy, int pageSize, int pageNumber);

	/**
	 * 根据条件查找,不支持排序
	 *
	 * @param condition
	 * @return
	 */
	List<T> find(T condition);

	/**
	 * 根据条件查找
	 *
	 * @param condition
	 * @return
	 */
	List<T> findByCondition(Condition condition);

	/**
	 * 带条件的分页查询,并排序
	 *
	 * @param condition
	 * @param orderBy
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	PageInfo<T> findByCondition(Condition condition, OrderBy orderBy, int pageSize, int pageNumber);


	/**
	 * 带条件的分页查询
	 *
	 * @param condition
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	PageInfo<T> findByCondition(Condition condition, int pageSize, int pageNumber);


	/**
	 * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
	 *
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws TooManyResultsException
	 */
	T findBy(String fieldName, Object value) throws TooManyResultsException;

	/**
	 * 通过多个ID查找//eg：ids -> “1,2,3,4”
	 *
	 * @param ids
	 * @return
	 */
	List<T> findByIds(String ids);

	/**
	 * 统计总记录
	 *
	 * @return
	 */
	int count();

	/**
	 * 统计总记录
	 *
	 * @param column
	 * @return
	 */
	int count(String column);

	/**
	 * 根据条件筛选统计
	 *
	 * @param condition
	 * @return
	 */
	int count(T condition);

	/**
	 * 根据条件筛选
	 *
	 * @param condition
	 * @return
	 */
	int count(Condition condition);


	/**
	 * 更新
	 *
	 * @param model
	 * @return
	 */
	int update(T model);

	/**
	 * 更新
	 *
	 * @param model
	 * @param id
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	void update(T model, PK id) throws IllegalAccessException, NoSuchFieldException;

	/**
	 * 根据条件更新
	 *
	 * @param model
	 * @param condition
	 */
	void update(T model, Condition condition);


	/**
	 * 通过主鍵刪除
	 *
	 * @param id
	 */
	void remove(PK id);

	/**
	 * 通过条件刪除
	 *
	 * @param condition
	 */
	void remove(T condition);

	/**
	 * 通过条件刪除
	 *
	 * @param condition
	 */
	void remove(Condition condition);

	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	void removeByIds(String ids);

	/**
	 * 批量删除
	 *
	 * @param idList
	 */
	void removeByIdList(List<PK> idList);


}

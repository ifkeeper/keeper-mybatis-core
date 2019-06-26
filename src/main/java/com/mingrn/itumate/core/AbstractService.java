package com.mingrn.itumate.core;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mingrn.itumate.global.exception.ServiceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public abstract class AbstractService<T, DTO extends T, PK extends Serializable> implements Service<T, DTO, PK> {

	@Resource
	protected Mapper<T> mapper;

	/**
	 * 当前泛型真实类型的Class
	 */
	private Class<T> modelClass;

	@SuppressWarnings("unchecked")
	public AbstractService() {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	@Override
	public void insert(T entity) {
		mapper.insertSelective(entity);
	}

	@Override
	public void batchInsert(List<T> entities) {
		mapper.insertList(entities);
	}

	@Override
	public T get(PK id) {
		return mapper.selectByPrimaryKey(id);
	}


	@Override
	public void remove(PK id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void remove(T condition) {
		if (condition == null) {
			throw new ServiceException("the condition is null when deleting the records!");
		}
		mapper.delete(condition);
	}

	@Override
	public void remove(Condition condition) {
		if (condition == null) {
			throw new ServiceException("the condition is null when deleting the records!");
		}
		mapper.deleteByCondition(condition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeByIds(String ids) {
		List idList = Arrays.asList(ids.split(","));
		removeByIdList((List<PK>) idList);
	}

	@Override
	public void removeByIdList(List<PK> idList) {
		List<String> ids = idList.stream().map(o -> "'" + o + "'").collect(Collectors.toList());
		mapper.deleteByIds(String.join(",", ids));
	}

	@Override
	public int update(T model) {
		return mapper.updateByPrimaryKeySelective(model);
	}

	@Override
	public void update(T model, PK id) {
		try {
			Field field = modelClass.getDeclaredField("id");
			field.setAccessible(true);
			field.set(model, id);
			mapper.updateByPrimaryKeySelective(model);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void update(T model, Condition condition) {
		if (condition == null) {
			throw new ServiceException("the condition is null when updating the records!");
		}
		mapper.updateByConditionSelective(model, condition);
	}


	@Override
	public T findBy(String fieldName, Object value) throws TooManyResultsException {
		try {
			T model = modelClass.newInstance();
			Field field = modelClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(model, value);
			return mapper.selectOne(model);
		} catch (ReflectiveOperationException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<T> findByIds(String ids) {
		return mapper.selectByIds(ids);
	}

	@Override
	public int count() {
		return mapper.selectCount(null);
	}

	@Override
	public int count(String column) {
		Condition condition = new Condition(modelClass);
		condition.setCountProperty(column);
		return mapper.selectCountByCondition(condition);
	}

	@Override
	public int count(T condition) {
		return mapper.selectCount(condition);
	}

	@Override
	public int count(Condition condition) {
		return mapper.selectCountByCondition(condition);
	}

	@Override
	public List<T> findByCondition(Condition condition) {
		return mapper.selectByCondition(condition);
	}

	@Override
	public PageInfo<T> findByCondition(Condition condition, OrderBy orderBy, int pageSize, int pageNumber) {
		if (orderBy != null) {
			condition = condition == null ? (new Condition(modelClass)) : condition;
			condition.setOrderByClause(orderBy.toString());
		}
		if (pageSize > 0 && pageNumber >= 0) {
			PageHelper.startPage(pageNumber, pageSize);
		}
		List<T> list = mapper.selectByCondition(condition);
		return new PageInfo<>(list);
	}


	@Override
	public PageInfo<T> findByCondition(Condition condition, int pageSize, int pageNumber) {
		return this.findByCondition(condition, null, pageSize, pageNumber);
	}

	@Override
	public PageInfo<T> find(int pageSize, int pageNumber) {
		return this.findByCondition(null, null, pageSize, pageNumber);
	}

	@Override
	public PageInfo<T> find(OrderBy orderBy, int pageSize, int pageNumber) {
		return this.findByCondition(null, orderBy, pageSize, pageNumber);
	}

	@Override
	public List<T> find(T condition) {
		return mapper.select(condition);
	}

	@Override
	public List<T> find() {
		return mapper.selectAll();
	}

	@Override
	public List<T> find(OrderBy orderBy) {
		Condition condition = new Condition(modelClass);
		condition.setOrderByClause(orderBy.toString());
		return mapper.selectByCondition(condition);
	}

	protected void setMybatisPageParams(int pageSize, int pageNumber, Map<String, Object> params) {
		if (pageSize > 0 && pageNumber >= 0) {
			int startRow = pageSize * (pageNumber - 1) + 1;
			int endRow = pageSize * pageNumber;
			int offset = pageSize * (pageNumber - 1);

			params.put("startRow", startRow);
			params.put("endRow", endRow);
			params.put("offset", offset);
			params.put("limit", pageSize);
		}
	}

	/**
	 * 封装查询参数
	 *
	 * @param condition
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	protected Map<String, Object> setMybatisPageParams(T condition, int pageSize, int pageNumber) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("condition", condition);
		this.setMybatisPageParams(pageSize, pageNumber, params);
		return params;
	}

	/**
	 * 封装查询参数
	 *
	 * @param condition
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	protected Map<String, Object> setMybatisPageParams(T condition, OrderBy orderBy, int pageSize, int pageNumber) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("condition", condition);
		this.setMybatisPageParams(pageSize, pageNumber, params);
		setMybatisPageParams(orderBy, params);
		return params;
	}

	/**
	 * 封装查询参数
	 *
	 * @param condition
	 * @return
	 */
	protected Map<String, Object> setMybatisPageParams(T condition) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("condition", condition);
		return params;
	}

	/**
	 * 封装查询参数
	 *
	 * @param condition
	 * @return
	 */
	protected Map<String, Object> setMybatisPageParams(T condition, OrderBy orderBy) {
		Map<String, Object> params = setMybatisPageParams(condition);
		if (orderBy != null) {
			params.put("orderBy", orderBy.toString());
		}
		return params;
	}

	/**
	 * 封装查询参数
	 *
	 * @return
	 */
	private Map<String, Object> setMybatisPageParams(OrderBy orderBy, Map<String, Object> params) {
		if (orderBy != null) {
			params.put("orderBy", orderBy.toString());
		}
		return params;
	}
}
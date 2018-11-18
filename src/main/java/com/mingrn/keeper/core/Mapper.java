package com.mingrn.keeper.core;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * MyBatis tkMapper插件接口,如需其他接口参考官方文档自行添加。
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public interface Mapper<T> extends BaseMapper<T>, ConditionMapper<T>,
		IdsMapper<T>, InsertListMapper<T>, SelectCountMapper<T>, SelectCountByConditionMapper<T> {
}
package com.codeworld.fc.common.core;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 业务中自己封装的Mapper
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    /**
     * 通过Id来判断一条数据是否存在
     *
     * @param id 对应的Id的值
     * @return
     */
    default boolean existsById(Serializable id) {
        return existsByColumn("id", id);
    }

    /**
     * 通过字段名和字段值来判断一条数据是否存在
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default boolean existsByColumn(String columnName, Serializable columnValue) {
        return countByColumn(columnName, columnValue) >= 1;
    }

    /**
     * 通过wrapper判断数据是否存在
     *
     * @param wrapper
     * @return
     */
    default boolean existsByWrapper(Wrapper<T> wrapper) {
        return selectCount(wrapper) > 0;
    }

    /**
     * 通过通过字段来查询满足条件的行数
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default int countByColumn(String columnName, Serializable columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().eq(columnName, columnValue);
        return selectCount(queryWrapper);
    }

    /**
     * 通过字段来查询模糊匹配的行数
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default int countByColumnLike(String columnName, Serializable columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().like(columnName, columnValue);
        return selectCount(queryWrapper);
    }

    /**
     * 通过字段名查询是否
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default boolean existsByColumnLike(String columnName, String columnValue) {
        return countByColumnLike(columnName, columnValue) >= 1;
    }

    /**
     * 通过单个字段来获取单个对应数据
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default T selectOneByColumn(String columnName, Serializable columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().eq(columnName, columnValue);
        return selectOne(queryWrapper);
    }

    /**
     * 通过单个字段获取所有对应数据
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default List<T> selectListByColumn(String columnName, Serializable columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().eq(columnName, columnValue);
        return selectList(queryWrapper);
    }

    /**
     * 通过字段名和字段值分页获取数据
     *
     * @param page
     * @param columnName
     * @param columnValue
     * @param <E>
     * @return
     */
    default <E extends IPage<T>> E selectPageByColumn(E page, String columnName, String columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().eq(columnName, columnValue);
        ;
        return selectPage(page, queryWrapper);
    }

    /**
     * 通过单个字段模糊匹配获取所有对应数据
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default List<T> selectListByColumnLike(String columnName, String columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().like("columnName", columnValue);
        return selectList(queryWrapper);
    }

    /**
     * 通过多个id字段匹配获取所有对应数据
     *
     * @param ids
     * @return
     */
    default List<T> selectListByIds(List<Long> ids) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().in("id", ids);
        return selectList(queryWrapper);
    }

    /**
     * 通过字段名和字段值模糊匹配分页获取数据
     *
     * @param page
     * @param columnName
     * @param columnValue
     * @param <E>
     * @return
     */
    default <E extends IPage<T>> E selectPageByColumnLike(E page, String columnName, String columnValue) {
        Wrapper<T> queryWrapper = new QueryWrapper<T>().like(columnName, columnValue);
        return selectPage(page, queryWrapper);
    }

    /**
     * 通过Id获取一条数据，返回Optional
     *
     * @param id
     * @return
     */
    default Optional<T> findById(Serializable id) {
        T t = selectById(id);
        return Optional.<T>ofNullable(t);
    }

    /**
     * 通过字段名返回一条数据，返回Optional
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    default Optional<T> findByColumn(String columnName, Serializable columnValue) {
        T t = selectOneByColumn(columnName, columnValue);
        return Optional.<T>ofNullable(t);
    }

    /**
     * 通过字段名删除数据
     *
     * @param columnName
     * @param columnValue
     */
    default void deleteByColumn(String columnName, Serializable columnValue) {
        Wrapper<T> wrapper = new QueryWrapper<T>().eq(columnName, columnValue);
        delete(wrapper);
    }
}

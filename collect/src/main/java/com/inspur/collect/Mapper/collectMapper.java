package com.inspur.collect.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface collectMapper {

    /**
     * 根据表名获取最后一次的采集ID
     * @param tableName
     * @return
     */
    Map getOldIdByTableName(String tableName);

    /**
     * 根据表名插入最后一次的采集ID
     * @param paramMap
     * @return
     */
    int insertOldIdByTableName(Map paramMap);

    /**
     * 根据表名更新最后一次的采集ID
     * @param paramMap
     * @return
     */
    int updateOldIdByTableName(Map paramMap);

    /**
     * 根据原始表名获取原始表的最后一条记录的Id
     * @return
     */
    Map getLastIdByTableName(@Param("tableName") String tableName);

    /**
     * 根据原始表名获取原始表数据
     * @param paramMap
     * @return
     */
    List<Map> tableListByTableName (Map paramMap);

    /**
     * 向采集表插入一条记录
     * @param paramMap
     * @return
     */
    int insertCollectOne (Map paramMap);

    /**
     * 根据采集表名获取最新一批的采集表记录
     * @Param tableName
     * @return
     */
    List<Map> collectListByTableName (Map paramMap);

    /**
     * 根据原始表名向原始表插入数据
     * @param paramMap
     * @return
     */
    int insertTableInfoByTableName (Map paramMap);
}

package com.zx.play.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @author zx
 * @create 2022/9/14 19:57
 */
public interface CkMapper {
    public void createWildTable(@Param("tableName") String name,@Param("tagName") String tagNames);

    public void dropTable(@Param("tableName") String name);

    public void insert(@Param("tableName") String name,
            @Param("tagetName") String name2,
            @Param("tagSql") String tagSql,
                       @Param("do_date") String do_date);

    void deleteBitMapByDate(@Param("table") String table,@Param("dt") String date);
}

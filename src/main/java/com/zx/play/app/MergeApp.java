package com.zx.play.app;

import com.zx.play.utils.PropertiesUtil;
import com.zx.play.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zx
 * @create 2022/9/13 20:18
 */
public class MergeApp {
    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME","atguigu");
        Integer taskId = Integer.parseInt(args[0]);
        String do_date = args[1];

        SparkSession spark = SessionUtil.getSparkSql("merge");

        List<String> tagTableNames = SessionUtil.getTagTableName(spark);
        String createSql = getCreateSql(tagTableNames, do_date);

        spark.sql(createSql).show();
        String insertSql = getInsertSql(tagTableNames, do_date);
        spark.sql(insertSql);

    }

    //生成建表语句
    public static String getCreateSql(List<String> tagTableNames,String do_date){
        String sql ="create table  if not exists %s.%s ( uid string, %s ) " +
                "comment '标签宽表' " +
                "row format delimited fields terminated by '\\t' " +
                "location '%s/%s'";

        String updbname = PropertiesUtil.getValue("updbname");
        //生成宽表前缀
        String s = PropertiesUtil.getValue("upwideprefix")
                + do_date.replace("-", "_");
        List<String> collect = tagTableNames.stream().map(tag -> tag + " string").collect(Collectors.toList());
        String join = StringUtils.join(collect, ",");
        String hdfsPath = PropertiesUtil.getValue("hdfsPath");
        sql = String.format(sql,updbname,s,join,hdfsPath,s);

        return sql;

    }

    //生成插入数据语句
    public static String getInsertSql(List<String> tableNames,String do_date){
        String tmp = "insert overwrite table %s.%s " +
                "select * from  ( %s ) t1 pivot ( min(tag_value) for tag_code in ( %s  )   )" ;
        String updbname = PropertiesUtil.getValue("updbname");

        String Tname = PropertiesUtil.getValue("upwideprefix")
                + do_date.replace("-", "_");
        String tmpInSql = "select uid, tag_value ,'%s' tag_code  from  %s.%s where dt='%s'";
        List<String> collect = tableNames.stream().map(v -> String
                .format(tmpInSql, v, updbname, v,do_date)).collect(Collectors.toList());
        String insql = StringUtils.join(collect," union all ");

        List<String> collect1 = tableNames.stream().map(v -> String.format("'%s'", v)).collect(Collectors.toList());
        String lastSql = StringUtils.join(collect1,",");

        tmp = String.format(tmp,updbname,Tname,insql,lastSql);
        return tmp;
    }



}

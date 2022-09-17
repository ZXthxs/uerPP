package com.zx.play.utils;

import org.apache.commons.codec.StringEncoder;
import org.apache.hadoop.hive.ql.udf.generic.Collector;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.expressions.Encode;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zx
 * @create 2022/9/11 15:43
 */
public class SessionUtil {

    public static List<String> getTagTableName(SparkSession sparkSession){

        String sql = "show tables in " + PropertiesUtil.getValue("updbname");
        Dataset<Row> sql1 = sparkSession.sql(sql);

        String upwideprefix = PropertiesUtil.getValue("upwideprefix");
        List<String> list = sql1
                .map((MapFunction<Row, String>) value -> value.getString(1), Encoders.STRING())
                .filter((FilterFunction<String>) value -> !value.startsWith(upwideprefix))
                .collectAsList();

        return list;
    }


    public static SqlSessionFactory getSqlSession(String config) throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream(config);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        return build;

    }

    public static SparkSession getSparkSql(String appName){
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .set("spark.sql.warehouse.dir",PropertiesUtil.getValue("hiveWarehouse"))
                .setMaster(PropertiesUtil.getValue("masterUrl"));
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport()
                .getOrCreate();
        return sparkSession;
    }


}

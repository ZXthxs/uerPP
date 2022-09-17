package com.zx.play.app;

import com.alibaba.fastjson.JSON;
import com.zx.play.mappers.CkMapper;
import com.zx.play.utils.PropertiesUtil;
import com.zx.play.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.json4s.scalap.$tilde;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zx
 * @create 2022/9/14 20:03
 */
public class ToCk {
    public static void main(String[] args) throws IOException {

        int i = Integer.parseInt(args[0]);
        String do_date = args[1];

        SqlSessionFactory sqlSession = SessionUtil.getSqlSession("clickhouse-config.xml");
        CkMapper ckMapper = sqlSession.openSession().getMapper(CkMapper.class);

        SparkSession tock = SessionUtil.getSparkSql("Tock");
        List<String> tagNames = SessionUtil.getTagTableName(tock);

        exToCk(ckMapper,tagNames, do_date,tock);

    }

    public static void exToCk(CkMapper ckMapper,List<String> tagNames,String do_date,SparkSession tock){
        //获得宽表名
        String temp = PropertiesUtil.getValue("upwideprefix");
        String tableName = temp + do_date.replace("-","_");
        //先删除
        ckMapper.dropTable(tableName);

        //建表
         //获得字段名
        List<String> collect = tagNames.stream().map(v -> v + " String ").collect(Collectors.toList());

        String join = StringUtils.join(collect, ",");
        ckMapper.createWildTable(tableName,join);

        //插入数据
        Dataset<Row> sql1 = tock.sql("select * from " +
                PropertiesUtil.getValue("updbname")+"."+tableName);

        sql1.write()
            .mode(SaveMode.Append)
            .option("driver",PropertiesUtil.getValue("ck.jdbc.driver.name"))
            .option("batchsize",500)
            .option("isolationLevel","NONE")   //ck没有事务，是OLAP 。事务关闭
            .option("numPartitions", "4") // 设置并发
            .jdbc(PropertiesUtil.getValue("ck.jdbc.url"),tableName,new Properties());







//        String sql = "insert into table %s values %s";
//        Dataset<Row> sql1 = tock.sql("select * from " +
//                PropertiesUtil.getValue("updbname")+"."+tableName);
//
//        List<String> list = sql1.map(new MapFunction<Row, String>() {
//            @Override
//            public String call(Row value) throws Exception {
//
//                return value.json();
//            }
//        }, Encoders.STRING()).collectAsList();
//
//        String result = "";
//        for (String s : list) {
//            Collection<Object> values = JSON.parseObject(s).values();
//            List<String> collect1 = values.stream().map(v -> {
//                if (v == null) {
//                    return "";
//                } else {
//                    return (String) v;
//                }
//            }).collect(Collectors.toList());
//            result+="(" + StringUtils.join(collect1,",")+"),";
//        }
//        sql = String.format(sql,tableName,result+"\b");
//
//        ckMapper.insert(tableName,result+"\b");
    }
}

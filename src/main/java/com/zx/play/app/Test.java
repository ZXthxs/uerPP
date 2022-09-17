package com.zx.play.app;

import com.zx.play.bean.TagInfo;
import com.zx.play.mappers.CkMapper;
import com.zx.play.mappers.TagInfoMapper;
import com.zx.play.utils.SessionUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zx
 * @create 2022/9/11 18:50
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        System.setProperty("HADOOP_USER_NAME","atguigu");
//        SparkSession a1 = SessionUtil.getSparkSql("a");
////        String a = "select id uid, \n" +
////                "       nvl(gender,'U') tag_value\n" +
////                " from   dim_user_zip where dt='9999-12-31';";
////        System.out.println(a.replaceAll("from[ ]*", "from gmall."));
//        System.out.println(SessionUtil.getTagTableName(a1));

//        SqlSessionFactory sqlSession = SessionUtil.getSqlSession("clickhouse-config.xml");
//        CkMapper mapper = sqlSession.openSession().getMapper(CkMapper.class);
//        //mapper.createWildTable("hello","nihao String");
//
//        mapper.dropTable("hello");

        SqlSessionFactory sqlSession = SessionUtil.getSqlSession("mysql-config.xml");
        TagInfoMapper mapper = sqlSession.openSession().getMapper(TagInfoMapper.class);

        System.out.println(mapper.getTagInfoOn());


    }
}

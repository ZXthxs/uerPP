package com.zx.play.app;

import com.zx.play.bean.TagInfo;
import com.zx.play.mappers.CkMapper;
import com.zx.play.service.MysqlDBService;
import com.zx.play.utils.PropertiesUtil;
import com.zx.play.utils.SessionUtil;
import com.zx.play.utils.TagValueType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zx
 * @create 2022/9/15 14:20
 */
public class BitMapApp {
    public static void main(String[] args) throws IOException {
        int id = Integer.parseInt(args[0]);
        String do_date = args[1];

        SqlSessionFactory sqlSession = SessionUtil.getSqlSession("mysql-config.xml");
        SparkSession sparkSession = SessionUtil.getSparkSql("GenerateBitmapApp");

        MysqlDBService mysqlDBService = new MysqlDBService(sqlSession.openSession());
        List<TagInfo> tagsOn = mysqlDBService.getTagOn();

        List<String> toDate = new ArrayList<>();
        List<String> toDecimal = new ArrayList<>();
        List<String> toLong = new ArrayList<>();
        List<String> toString = new ArrayList<>();
        for (TagInfo tagInfo : tagsOn) {
            TagValueType x=TagValueType.getTagValueTypeByInfo(tagInfo.getTagValueType());
            switch (x){
                case TAG_VALUE_TYPE_STRING : toString.add(tagInfo.getTagCode().toLowerCase());break;
                case TAG_VALUE_TYPE_DECIMAL : toDecimal.add(tagInfo.getTagCode().toLowerCase());break;
                case TAG_VALUE_TYPE_DATE:  toDate.add(tagInfo.getTagCode().toLowerCase());break;
                case TAG_VALUE_TYPE_LONG: toLong.add(tagInfo.getTagCode().toLowerCase());break;
            }
        }

        CkMapper mapper = SessionUtil.getSqlSession("clickhouse-config.xml").openSession().getMapper(CkMapper.class);

        insertCkWildTable(toDate,do_date,mapper,"user_tag_value_date");
        insertCkWildTable(toString, do_date, mapper,"user_tag_value_string");
        insertCkWildTable(toDecimal,do_date,mapper,"user_tag_value_decimal");
        insertCkWildTable(toLong,do_date,mapper,"user_tag_value_long");

    }

    public static void insertCkWildTable(List<String> tags, String do_date,  CkMapper mapper,String tagetName) {
        if(tags.size()>0){
            String temp = "('%s',%s)";
            List<String> collect = tags.stream().map(v -> String.format(temp, v, v)).collect(Collectors.toList());
            String tagSql = StringUtils.join(collect, ",");

            String tableName = PropertiesUtil.getValue("upwideprefix") + do_date.replace("-", "_");


        mapper.deleteBitMapByDate(tagetName,do_date);
        mapper.insert(tableName,tagetName,tagSql,do_date);


        }

    }

}

package com.zx.play.app;

import com.zx.play.bean.TagInfo;
import com.zx.play.bean.TaskInfo;
import com.zx.play.bean.TaskTagRule;
import com.zx.play.service.MysqlDBService;
import com.zx.play.utils.PropertiesUtil;
import com.zx.play.utils.SessionUtil;
import com.zx.play.utils.TagValueType;
import org.apache.hadoop.yarn.webapp.Controller;
import org.apache.ibatis.session.SqlSession;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author zx
 * @create 2022/9/11 15:48
 */
public class MyApp {
    public static void main(String[] args) throws IOException {
        Integer taskId = Integer.parseInt(args[0]);
        String do_date = args[1];

        SqlSession mysql = SessionUtil.getSqlSession("mysql-config.xml").openSession();
        MysqlDBService mysqlDBService = new MysqlDBService(mysql);

        TaskInfo taskInfo = mysqlDBService.getTaskInfoById(taskId);
        TagInfo tagInfo = mysqlDBService.getTagInfoById(taskId);
        List<TaskTagRule> taskTagRules = mysqlDBService.getTaskTagRuleById(taskId);
        List<TaskInfo> allTask = mysqlDBService.getAllTask();

        SparkSession spark = SessionUtil.getSparkSql("a");
        //建表
        spark.sql(createTableSql(taskInfo,tagInfo));
        //插数据
        spark.sql(getInsertSql(tagInfo,taskInfo,taskTagRules,do_date));
    }

    public static String createTableSql(TaskInfo taskInfo, TagInfo tagInfo){
        String sql = "CREATE EXTERNAL TABLE if not exists %s.%s" +
                "(" +
                "    uid string," +
                "    tag_value %s COMMENT '%s'\n" +
                ")" +
                "    PARTITIONED BY (dt STRING)\n" +
                "    row format delimited fields terminated by '\\t'" +
                "    LOCATION '%s/%s';";
        String dbname = PropertiesUtil.getValue("updbname");
        String tableName = tagInfo.getTagCode();
        String tag_value_type=null;
        TagValueType x=TagValueType.getTagValueTypeByInfo(tagInfo.getTagValueType());
        switch (x){
            case TAG_VALUE_TYPE_STRING : tag_value_type="string";break;
            case TAG_VALUE_TYPE_DECIMAL : tag_value_type="decimal(16,2)";break;
            case TAG_VALUE_TYPE_DATE:  tag_value_type="double";break;
            case TAG_VALUE_TYPE_LONG: tag_value_type="long";break;
        }
        String taskComment = taskInfo.getTaskComment();
        String hdfsPath = PropertiesUtil.getValue("hdfsPath");
        sql=String.format(sql,dbname,tableName,tag_value_type,taskComment,hdfsPath,tableName);

        return sql;
    }

    public static String getInsertSql(TagInfo tagInfo,TaskInfo taskInfo,
                                      List<TaskTagRule> taskTagRules,String do_date){
        String sql = " insert overwrite table %s.%s partition (dt = '%s')\n" +
                "select" +
                "    uid," +
                "       %s " +
                "from (%s) tmp";
        String dbname = PropertiesUtil.getValue("updbname");
        String tableName = tagInfo.getTagCode();
        String tag_value ="";
        if (taskTagRules.size()>0){
            List<String> collect = taskTagRules.stream().map(
                    taskTagRule -> String.format("when '%s' then '%s'",
                            taskTagRule.getQueryValue(), taskTagRule.getSubTagValue())
            ).collect(Collectors.toList());

            tag_value="case tag_value " + String.join(" ",collect)+ " end";

        }else {tag_value = "tag_value";}

        String taskSql =  taskInfo.getTaskSql().replace(";","")
                .replace("$do_date",do_date).replaceAll("from[ ]*","from %s.");
        taskSql=String.format(taskSql,PropertiesUtil.getValue("warehouse_dbname"));
        sql = String.format(sql,dbname,tableName,do_date,tag_value,taskSql);

        return sql;
    }


}

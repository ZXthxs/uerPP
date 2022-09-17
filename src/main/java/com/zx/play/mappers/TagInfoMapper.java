package com.zx.play.mappers;

import com.zx.play.bean.TagInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zx
 * @create 2022/9/11 15:08
 */
public interface TagInfoMapper {

    TagInfo getTagInfoById(@Param("id") Integer taskId);

    List<TagInfo> getTagInfoOn();

}

package com.czxy.xuecheng.mange_course.dao;

import com.czxy.xuecheng.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeachplanMapper {
    // 课程计划查询
    public TeachplanNode selectList(@Param("courseId") String courseId);
}

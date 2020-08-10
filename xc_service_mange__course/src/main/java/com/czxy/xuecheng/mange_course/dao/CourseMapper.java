package com.czxy.xuecheng.mange_course.dao;

import com.czxy.xuecheng.domain.course.ext.CourseInfo;
import com.czxy.xuecheng.domain.course.request.CourseListRequest;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CourseMapper {

    // 我的课程查询列表
    public Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}

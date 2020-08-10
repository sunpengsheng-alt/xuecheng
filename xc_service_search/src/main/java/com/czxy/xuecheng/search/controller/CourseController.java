package com.czxy.xuecheng.search.controller;

import com.czxy.xuecheng.api.search.CourseControllerApi;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.domain.search.Course;
import com.czxy.xuecheng.domain.search.CourseSearchParam;
import com.czxy.xuecheng.search.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search/course")
public class CourseController implements CourseControllerApi {

    @Resource
    private CourseService coursePublishService;

    @Override
    @GetMapping(value = "/list/{page}/{size}")
    public QueryResponseResult<Course> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return coursePublishService.list(page,size,courseSearchParam);
    }
}

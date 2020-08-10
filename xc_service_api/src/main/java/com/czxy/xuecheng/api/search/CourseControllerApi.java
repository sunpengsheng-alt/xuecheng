package com.czxy.xuecheng.api.search;

import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.domain.search.Course;
import com.czxy.xuecheng.domain.search.CourseSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "课程搜索接口",description = "完成课程的搜索服务")
public interface CourseControllerApi {

    @ApiOperation("课程综合搜索")
    public QueryResponseResult<Course> list(int page, int size, CourseSearchParam courseSearchParam);
}

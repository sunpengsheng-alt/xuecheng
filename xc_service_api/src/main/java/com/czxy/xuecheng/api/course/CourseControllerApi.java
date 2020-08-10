package com.czxy.xuecheng.api.course;

import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.course.Teachplan;
import com.czxy.xuecheng.domain.course.ext.CourseInfo;
import com.czxy.xuecheng.domain.course.ext.CourseView;
import com.czxy.xuecheng.domain.course.ext.TeachplanNode;
import com.czxy.xuecheng.domain.course.request.CourseListRequest;
import com.czxy.xuecheng.domain.course.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口",description = "提供课程的增、删、改、查操作")
public interface CourseControllerApi {

    @ApiOperation("查询所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int"),
    })
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("课程计划查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="courseId",value = "课程ID",required=true,paramType="path",dataType="string")
    })
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询课程图片")
    public ResponseResult findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult delCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId",value = "ID",required = true,paramType = "path",dataType = "string")
    })
    public CourseView courseview(String courseId);

    @ApiOperation("预览课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId",value = "ID",required = true,paramType = "path",dataType = "string")
    })
    public CoursePublishResult preview(String courseId);

    @ApiOperation("课程发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId",value = "课程ID",required = true,paramType = "path",dataType = "string")
    })
    public CoursePublishResult publish(String courseId);
}

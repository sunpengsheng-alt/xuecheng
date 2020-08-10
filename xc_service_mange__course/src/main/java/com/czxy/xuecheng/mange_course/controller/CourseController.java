package com.czxy.xuecheng.mange_course.controller;

import com.czxy.xuecheng.api.course.CourseControllerApi;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.course.Teachplan;
import com.czxy.xuecheng.domain.course.ext.CourseInfo;
import com.czxy.xuecheng.domain.course.ext.CourseView;
import com.czxy.xuecheng.domain.course.ext.TeachplanNode;
import com.czxy.xuecheng.domain.course.request.CourseListRequest;
import com.czxy.xuecheng.domain.course.response.CoursePublishResult;
import com.czxy.xuecheng.mange_course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Resource
    private CourseService courseService;

    /**
     * 查询我的课程
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,@PathVariable("size") int size, CourseListRequest courseListRequest) {
        String company_id = "1";
        return courseService.findCourseList(company_id,page,size,courseListRequest);
    }

    /**
     * 查询课程计划
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**
     * 添加课程图片
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId,String pic) {
        System.out.println(courseId+"-=-=--=-=");
        return courseService.addCoursePic(courseId,pic);
    }

    /**
     * 查询课程图片
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public ResponseResult findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("/coursepic/del/{courseId}")
    public ResponseResult delCoursePic(@PathVariable("courseId") String courseId) {
        System.err.println(courseId);
        return courseService.delCoursePic(courseId);
    }

    /**
     * 查询课程视图
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("courseview/{courseId}")
    public CourseView courseview(@PathVariable("courseId") String courseId) {
        return courseService.getVourseView(courseId);
    }

    /**
     * 课程预览
     * @param courseId
     * @return
     */
    @Override
    @PostMapping("/preview/{courseId}")
    public CoursePublishResult preview(@PathVariable("courseId") String courseId) {
        return courseService.preview(courseId);
    }

    /**
     * 课程发布
     * @param courseId
     * @return
     */
    @Override
    @PostMapping("/publish/{courseId}")
    public CoursePublishResult publish(@PathVariable("courseId") String courseId) {
        System.out.println(courseId+"-=-=-=-=--=-=-=-=-=-=-=");
        return courseService.publish(courseId);
    }
}

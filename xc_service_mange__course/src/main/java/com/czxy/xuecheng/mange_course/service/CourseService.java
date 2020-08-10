package com.czxy.xuecheng.mange_course.service;

import com.alibaba.fastjson.JSON;
import com.czxy.xuecheng.common.exception.ExceptionCast;
import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.QueryResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.cms.CmsPage;
import com.czxy.xuecheng.domain.cms.response.CmsPageResult;
import com.czxy.xuecheng.domain.cms.response.CmsPostPageResult;
import com.czxy.xuecheng.domain.course.*;
import com.czxy.xuecheng.domain.course.ext.CourseInfo;
import com.czxy.xuecheng.domain.course.ext.CourseView;
import com.czxy.xuecheng.domain.course.ext.TeachplanNode;
import com.czxy.xuecheng.domain.course.request.CourseListRequest;
import com.czxy.xuecheng.domain.course.response.CourseCode;
import com.czxy.xuecheng.domain.course.response.CoursePicResult;
import com.czxy.xuecheng.domain.course.response.CoursePublishResult;
import com.czxy.xuecheng.mange_course.dao.*;
import com.czxy.xuecheng.mange_course.feign.CmsPageFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    /**
     * 查询我的课程
     * @param company_id
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult<CourseInfo> findCourseList(String company_id, int page, int size, CourseListRequest courseListRequest){
//1 准备参数
        if(courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(company_id);

        //2 分页
        PageHelper.startPage(page, size);

        //3 查询 + 分页
        List<CourseInfo> list = courseMapper.findCourseListPage(courseListRequest);
        PageInfo<CourseInfo> pageInfo = new PageInfo<>(list);

        //4 封装
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(list);
        queryResult.setTotal(pageInfo.getTotal());
        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }

    @Resource
    private TeachplanMapper teachplanMapper;

    /**
     * 查询课程计划
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    @Resource
    private TeachplanRepository teachplanRepository;


    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan) {
        // 校验数据
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 准备数据
        // 获得课程id
        String courseid = teachplan.getCourseid();
        // 获得计划id,如果没有就创建新的
        String parentid = teachplan.getParentid();

        if(StringUtils.isEmpty(parentid)){
            // 取出该课程的根结点
            parentid = this.getTeachplanRoot(courseid);
        }

        // 查询已有计划
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();

        // 根据已有计划，组装新计划数据
        Teachplan teachplanNew = new Teachplan();
        // 将页面提交的teachplan信息拷贝到teachplanNew对象中
        BeanUtils.copyProperties(teachplan,teachplanNew);
        // 设置课程信息
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        // 设置级别，最多3级
        if(parentNode.getGrade().equals("1")){
            teachplanNew.setGrade("2");
        }else{
            teachplanNew.setGrade("3");
        }

        // 保存计划
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);


    }

    @Resource
    private CourseBaseRepository courseBaseRepository;


    /**
     * 获得课程信息
     * @param courseId
     * @return
     */
    private String getTeachplanRoot(String courseId) {
        // 查询课程
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(! optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 课程信息
        CourseBase courseBase = optional.get();
        // 查询课程的根结点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        // 如果不存在创建新的
        if(teachplanList == null || teachplanList.size() <= 0){
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        // 如果存在返回
        return teachplanList.get(0).getId();
    }

    @Resource
    private CoursePicRepository coursePicRepository;

    /**
     * 保存课程图片
     * @param courseId
     * @param pic
     * @return
     */
    public ResponseResult addCoursePic(String courseId,String pic){
        //1 封装对象
        CoursePic coursePic = null;
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            coursePic = optional.get();
        } else {
            coursePic = new CoursePic();
        }

        //2 更新数据 / 添加数据
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);

        //3 保存操作
        coursePicRepository.save(coursePic);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询课程图片
     * @param courseId
     * @return
     */
    public ResponseResult findCoursePic(String courseId) {
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            System.out.println(coursePic+"124356754324------------");
            return new CoursePicResult(CommonCode.SUCCESS,coursePic);
        }
        return new CoursePicResult(CommonCode.SUCCESS,null);
    }

    /**
     * 删除课程
     * @param courseId
     * @return
     */
    public ResponseResult delCoursePic(String courseId) {
        System.out.println(courseId);
        coursePicRepository.deleteById(courseId);
        return new CoursePicResult(CommonCode.SUCCESS,null);
    }

    @Resource
    private CourseMarketRespository courseMarketRespository;

    /**
     * 查询课程视图
     * @param courseId
     * @return
     */
    public CourseView getVourseView(String courseId) {
        // 1.1 创建封装对象
        CourseView courseView = new CourseView();

        // 1.2 查询课程基本信息
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            courseView.setCourseBase(courseBase);
        }

        // 1.3 查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(coursePic);
        }

        // 1.4 课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRespository.findById(courseId);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }

        // 1.5 课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    @Resource
    private CmsPageFeign cmsPageFeign;

    @Value("${course-publish.siteId}")
    public String publish_siteId;

    @Value("${course-publish.templateId}")
    public String publish_templateId;

    @Value("${course-publish.dataUrlPre}")
    public String publish_dataUrlPre;

    @Value("${course-publish.pagePhysicalPath}")
    public String publish_page_physicalpath;

    @Value("${course-publish.previewUrl}")
    public String previewUrl;

    @Value("${course-publish.pageWebPath}")
    public String publish_page_webPath;

    /**
     * 课程预览
     * @param courseId
     * @return
     */
    public CoursePublishResult preview(String courseId) {
        System.out.println(courseId+"================");
        // 1. 查询课程
        CourseBase courseBaseById = this.findCourseBaseById(courseId);

        // 2. 封装cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);                      // 站点id
        cmsPage.setTemplateId(publish_templateId);              // 页面模板id
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);      // 数据模型url
        cmsPage.setPageName(courseId + ".html");                // 页面名称
        cmsPage.setPageAliase(courseBaseById.getName());        // 页面别名,就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath); // 页面物理路径
        cmsPage.setPageWebPath(publish_page_webPath);           // 页面webpath

        // 3. 远程调用cms
        CmsPageResult cmsPageResult = cmsPageFeign.save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        // 4. 封装返回结果
        CmsPage findCmsPage = cmsPageResult.getCmsPage();
        String pageId = findCmsPage.getPageId();

        // 拼装页面预览的url
        String url = previewUrl + pageId;

        // 返回CoursePublishResult对象(当中包含了页面预览的url)
        return new CoursePublishResult(CommonCode.SUCCESS,url);
    }

    /**
     * 根据id查询课程基本信息
     * @param courseId
     * @return
     */
    private CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()){
            return baseOptional.get();
        }
        ExceptionCast.cast(CourseCode.COURSE_DENIED_DELETE);
        return null;
    }

    /**
     * 课程发布
     * @param courseId
     * @return
     */
    public CoursePublishResult publish(String courseId) {
        // 1. 查询课程
        CourseBase courseBase = this.findCourseBaseById(courseId);
        System.out.println(courseBase.getName()+"页面名称");
        // 2. 准备页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);                      // 站点id
        cmsPage.setTemplateId(publish_templateId);              // 页面模板id
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);      // 数据模型url
        cmsPage.setPageName(courseId + ".html");                // 页面名称
        cmsPage.setPageAliase(courseBase.getName());            // 页面别名,就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath); // 页面物理路径
        cmsPage.setPageWebPath(publish_page_webPath);           // 页面webpath
        // 3. 一键发布
        CmsPostPageResult cmsPostPageResult = cmsPageFeign.postPageQuick(cmsPage);
        if (! cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 4. 更新课程状态为"已发布"
        CourseBase courseBaseState = this.saveCoursePubState(courseId,"202002");
        if (courseBaseState == null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        // 保存发布详情
        // 创建coursePub对象,同使用其他4张表中的数据进行更新
        CoursePub coursePub = createCoursePub(courseId);
        // 将coursePub对象保存到数据库
        saveCoursePub(courseId,coursePub);
        // 5. 封装数据
        String previewUrl = cmsPostPageResult.getPageUrl();
        System.out.println(previewUrl);

        return new CoursePublishResult(CommonCode.SUCCESS,previewUrl);
    }

    @Resource
    private CoursePubRepository coursePubRepository;

    /**
     * 将coursePub对象保存到数据库
     * @param courseId
     * @param coursePub
     */
    private void saveCoursePub(String courseId, CoursePub coursePub) {
        // 1. 设置id
        coursePub.setId(courseId);
        // 2. 时间戳,给logstach使用
        coursePub.setTimestamp(new Date());
        // 3. 发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePub.setPubTime(date);
        // 4. 保存
        coursePubRepository.save(coursePub);
    }

    /**
     * 创建courseId对象
     * @param courseId
     * @return
     */
    private CoursePub createCoursePub(String courseId) {
        // 1. 创建封装对象
        CoursePub coursePub = new CoursePub();
        // 2.1 根据课程id查询course_base
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            // 将courseBase属性拷贝到COursePub中
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        // 2.2 查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }
        // 2.3 课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRespository.findById(courseId);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        // 2.4 课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        String jsonString = JSON.toJSONString(teachplanNode);
        // 将课程计划信息json串保存到course_pub中
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }



    /**
     * 修改课程状态
     * @param courseId
     * @param state
     * @return
     */
    private CourseBase saveCoursePubState(String courseId, String state) {
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus(state);
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }



}

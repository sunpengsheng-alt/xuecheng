package com.czxy.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.czxy.xuecheng.common.exception.ExceptionCast;
import com.czxy.xuecheng.common.exception.ExceptionCatch;
import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.QueryResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.cms.CmsConfig;
import com.czxy.xuecheng.domain.cms.CmsPage;
import com.czxy.xuecheng.domain.cms.CmsSite;
import com.czxy.xuecheng.domain.cms.CmsTemplate;
import com.czxy.xuecheng.domain.cms.request.QueryPageRequest;
import com.czxy.xuecheng.domain.cms.response.CmsCode;
import com.czxy.xuecheng.domain.cms.response.CmsPageResult;
import com.czxy.xuecheng.domain.cms.response.CmsPostPageResult;
import com.czxy.xuecheng.domain.cms.response.CmsTemplateResult;
import com.czxy.xuecheng.manage_cms.config.RabbitmqConfig;
import com.czxy.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.czxy.xuecheng.manage_cms.dao.CmsPageRepository;
import com.czxy.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.czxy.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {

    @Resource
    private CmsPageRepository cmsPageRepository;

    @Resource
    private CmsSiteRepository cmsSiteRepository;

    @Resource
    private CmsTemplateRepository cmsTemplateRepository;

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private ExceptionCatch exceptionCatch;

    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 页面查询方法
     * @param page 页码，从1开始记数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        // 1 自定义条件查询
        // 1.1 查询条件对象非空修复
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        // 1.2 定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 1.3 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置条件值（站点id）
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 设置模板id作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        // 设置页面别名作为查询条件
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // 1.4 定义条件对象Example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        // 1.5 分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);

        //2 查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询

        //3 结果封装：状态 + 数据
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 查询所有站点
     * @return
     */
    public QueryResponseResult<CmsSite> findAllSite(){
        List<CmsSite> list = this.cmsSiteRepository.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(list);
        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }

    /**
     * 查询所有模板
     * @return
     */
    public QueryResponseResult<CmsTemplate> findAllTemplate(){
        List<CmsTemplate> list = this.cmsTemplateRepository.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(list);
        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        //1 校验
        // 1.1 非空校验
        if (cmsPage == null) {
            //抛出异常
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 1.2 业务校验 pageName、siteid 、pageWebPath
        CmsPage findCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                cmsPage.getPageName(),
                cmsPage.getSiteId(),
                cmsPage.getPageWebPath());
        if(findCmsPage != null) {
            //抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);

        }

        //2 添加
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage );
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    public CmsPageResult findById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CmsCode.CMS_PAGE_NOTEXISTS,null);
    }

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id,CmsPage cmsPage){
        //根据id从数据库查询页面信息
        CmsPage one = this.findById(id).getCmsPage();
        if(one!=null){
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataUrl
            one.setDataUrl(cmsPage.getDataUrl());
            //提交修改
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);

    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage one = this.findById(id).getCmsPage();
        if (one == null) {
            return new ResponseResult(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public CmsTemplateResult upload(MultipartFile file) {
        try {
            // 向GridFs存储文件
            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            // 得到文件id
            CmsTemplate cmsTemplate = new CmsTemplate();
            cmsTemplate.setTemplateFileId(objectId.toString());
            return new CmsTemplateResult(CommonCode.SUCCESS,cmsTemplate);
        } catch (IOException e) {
            return new CmsTemplateResult(CommonCode.FAIL,null);
        }
    }


    /**
     * 页面静态化
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId){

        // 1.通过id查询页面CmsPage
        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (cmsPage == null){
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 2.通过dataUrl获取页面模型数据
        Map map = getModelByDataUrl(cmsPage.getDataUrl());
        if (map == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 3.通过模板id,获取模板内容
        String template = getTemplateByPageId(cmsPage.getTemplateId());
        if (StringUtils.isEmpty(template)) {
            // 页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 执行静态化
        String html = generateHtml(template,map);
        if (StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 获得油漆面模型数据
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (cmsPage == null){
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 取出dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        return entity.getBody();
    }

    /**
     * 页面静态化
     * @param template
     * @param map
     * @return
     */
    private String generateHtml(String template, Map map) {
        try {
            // 生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            // 模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("templates",template);
            // 配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            // 获取模板
            Template template1 = configuration.getTemplate("templates");
            // 5.通过工具类将模板对象转换成html代码
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1,map);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Resource
    private GridFSBucket gridFSBucket;
    /**
     * 获取页面模板
     * @param templateId
     * @return
     */
    private String getTemplateByPageId(String templateId) {
        try {
            // 1.通过id获得模板对象
            Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
            if (!optional.isPresent()){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
            }
            CmsTemplate cmsTemplate = optional.get();

            // 2.通过fileId,获得模板文件对象
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 2.1 取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            // 3.打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

            // 4.以资源的方式获得IO流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            // 5.将IO转换为字符串
            String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取页面模型数据
     * @param dataUrl
     * @return
     */
    private Map getModelByDataUrl(String dataUrl) {
        // 判断dataUrl是否为空
        if (StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        return entity.getBody();
    }

    @Resource
    private CmsConfigRepository cmsConfigRepository;


    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(! optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        CmsConfig cmsConfig = optional.get();
        return cmsConfig;
    }

    /**
     * 发布页面
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) {
        // 1. 执行页面静态化
        String pageHtml = getPageHtml(pageId);
        // 2. 查询CmsPage
        CmsPage cmsPage = findById(pageId).getCmsPage();
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 3. 将页面静态化文件存储到GrifFs中
        saveHtml(cmsPage,pageHtml);
        // 4. 向MQ发消息
        sendPostPage(cmsPage);

        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 向MQ发布消息
     * @param cmsPage
     */
    private void sendPostPage(CmsPage cmsPage) {
        // 1.生成消息
        // 1.1 准备数据
        HashMap<String, String> map = new HashMap<>();
        map.put("pageId",cmsPage.getPageId());
        // 1.2 转换json传
        String msg = JSON.toJSONString(map);
        // 2. 发送
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,cmsPage.getSiteId(),msg);
    }

    /**
     * 将页面静态化文件存储到GridFs中
     * @param cmsPage
     */
    private void saveHtml(CmsPage cmsPage,String pageHtml) {
        try {
            // 1. 删除静态页面
            String htmlFileId = cmsPage.getHtmlFileId();
            if (StringUtils.isNotEmpty(htmlFileId)){
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
            }
            // 2.保存页面到GridFs中
            InputStream inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
            ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());

            // 更新CmsPage
            cmsPage.setHtmlFileId(objectId.toHexString());
            cmsPageRepository.save(cmsPage);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL);
        }
    }

    /**
     * 将页面html保存到页面物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId){
        try {
            //1 html的文件id
            // 1.1 查询页面
            CmsPage cmsPage = findById(pageId).getCmsPage();

            // 1.2 获得文件id
            String htmlFileId = cmsPage.getHtmlFileId();

            //2 从gridFS获得文件流
            InputStream inputStream = this.getFileById(htmlFileId);

            //3 拼凑物理路径,页面物理路径=站点物理路径+页面物理路径+页面名称
            // 3.1 得到站点id
            String siteId = cmsPage.getSiteId();
            // 3.2 得到站点的信息
            CmsSite cmsSite = this.findCmsSiteById(siteId);
            // 3.3 得到站点的物理路径
            String sitePhysicalPath = cmsSite.getSitePhysicalPath();
            // 3.4 得到页面的物理路径
            String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

            //4 保存文件
            FileUtils.copyInputStreamToFile(inputStream,new File(pagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据站点id查询站点详情
     * @param siteId
     * @return
     */
    public CmsSite findCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 通过文件id从gridFS获得文件流
     * @param fileId
     * @return
     */
    private InputStream getFileById(String fileId) {
        try {
            //文件对象
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            //打开下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //定义GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage) {
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (page != null){
             // 更新
            return this.update(page.getPageId(),cmsPage);
        } else {
            // 添加
            return this.add(cmsPage);
        }
    }

    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        // 1.保存页面
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // 2. 得到页面的id
        CmsPage saveCmsPage = save.getCmsPage();
        String pageId = saveCmsPage.getPageId();

        // 3. 发布页面
        ResponseResult postPage = this.postPage(pageId);
        if (!postPage.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // 4. 得到页面的url
        // 拼接页面
        String siteId = saveCmsPage.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        // 页面url
        String pageUrl = cmsSite.getSiteDomain() + saveCmsPage.getPageWebPath() + saveCmsPage.getPageName();

        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }
}

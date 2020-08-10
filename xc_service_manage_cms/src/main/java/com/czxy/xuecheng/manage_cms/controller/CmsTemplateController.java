package com.czxy.xuecheng.manage_cms.controller;

import com.czxy.xuecheng.api.cms.CmsTemplateControllerApi;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.cms.CmsPage;
import com.czxy.xuecheng.domain.cms.CmsTemplate;
import com.czxy.xuecheng.domain.cms.request.QueryTemplateRequest;
import com.czxy.xuecheng.domain.cms.response.CmsPageResult;
import com.czxy.xuecheng.domain.cms.response.CmsTemplateResult;
import com.czxy.xuecheng.manage_cms.service.PageService;
import com.czxy.xuecheng.manage_cms.service.TemplateService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Resource
    private PageService pageService;

    @Resource
    private TemplateService templateService;


    /**
     * 条件查询 + 分页
     * @param page
     * @param size
     * @param queryTemplateRequest
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryTemplateRequest queryTemplateRequest) {
        return templateService.findListTemplate(page,size,queryTemplateRequest);
    }

    /**
     * 查询所有模板
     * @return
     */
    @GetMapping
    public QueryResponseResult<CmsTemplate> findAll(){
        return pageService.findAllTemplate();
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public CmsTemplateResult upload(MultipartFile file) {
        return  pageService.upload(file);
    }

    /**
     * 添加模板
     * @param cmsTemplate
     * @return
     */
    @PostMapping("/add")
    public CmsTemplateResult  add (@RequestBody CmsTemplate cmsTemplate) {
        return templateService.add(cmsTemplate);
    }

    /**
     * 删除模板
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return templateService.deleteTemplate(id);
    }

    @GetMapping("/get/{id}")
    public CmsTemplateResult findById(@PathVariable("id") String id) {
        return templateService.findById(id);
    }

    @PutMapping("/edit/{id}")
    public CmsTemplateResult update(@PathVariable("id")String id, @RequestBody CmsTemplate cmsTemplate) {
        return templateService.update(id,cmsTemplate);
    }

}

package com.czxy.xuecheng.manage_cms.service;

import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.QueryResult;
import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.domain.cms.CmsTemplate;
import com.czxy.xuecheng.domain.cms.request.QueryTemplateRequest;
import com.czxy.xuecheng.domain.cms.response.CmsCode;
import com.czxy.xuecheng.domain.cms.response.CmsTemplateResult;
import com.czxy.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class TemplateService {

    @Resource
    private CmsTemplateRepository cmsTemplateRepository;

    @Resource
    private GridFsTemplate gridFsTemplate;
    /**
     * 分页 + 查询
     * @param page
     * @param size
     * @param queryTemplateRequest  查询条件
     * @return
     */
    public QueryResponseResult findListTemplate(int page, int size, QueryTemplateRequest queryTemplateRequest) {
        // 1 自定义条件查询
        // 1.1 查询条件对象非空修复
        if(queryTemplateRequest == null){
            queryTemplateRequest = new QueryTemplateRequest();
        }
        // 1.2 定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("templateAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 1.3 条件值对象
        CmsTemplate template = new CmsTemplate();
        // 设置条件值（站点id）
        if(StringUtils.isNotEmpty(queryTemplateRequest.getSiteId())){
            template.setSiteId(queryTemplateRequest.getSiteId());
        }

        // 设置模板名称作为查询条件
        if(StringUtils.isNotEmpty(queryTemplateRequest.getTemplateName())){
            template.setTemplateName(queryTemplateRequest.getTemplateName());
        }




        // 1.4 定义条件对象Example
        Example<CmsTemplate> example = Example.of(template,exampleMatcher);

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
        Page<CmsTemplate> all = cmsTemplateRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询

        //3 结果封装：状态 + 数据
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 根据id查询模板
     * @param id
     * @return
     */
    public CmsTemplateResult findById(String id) {
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            return new CmsTemplateResult(CommonCode.SUCCESS,cmsTemplate);
        }
        return new CmsTemplateResult(CmsCode.CMS_PAGE_NOTEXISTS,null);
    }



    /**
     * 根据id删除模板
     * @param id
     * @return
     */
    public ResponseResult deleteTemplate(String id) {
        CmsTemplate one = this.findById(id).getCmsTemplate();
        if (one == null) {
            return new ResponseResult(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        cmsTemplateRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 添加模板
     * @param cmsTemplate
     * @return
     */
    public CmsTemplateResult add(CmsTemplate cmsTemplate) {
        System.out.println(cmsTemplate.toString()+"template添加操作");
        //1 校验
        // 1.1 非空校验
        if (cmsTemplate == null) {
            //抛出异常
            throw new RuntimeException("操作对象为空");
        }
        // 1.2 业务校验 siteId templateName templateParameter templateFileId
        CmsTemplate findcmsTemplate = cmsTemplateRepository.findBysiteIdAndTemplateNameAndTemplateParameterAndTemplateFileId(
           cmsTemplate.getSiteId(),
           cmsTemplate.getTemplateName(),
           cmsTemplate.getTemplateParameter(),
           cmsTemplate.getTemplateFileId()
        );
        if(findcmsTemplate != null) {
            //抛出异常
            throw new RuntimeException("操作对象已存在");
        }

        //2 添加
        cmsTemplateRepository.save(cmsTemplate);
        return new CmsTemplateResult(CommonCode.SUCCESS, cmsTemplate );
    }

    public CmsTemplateResult update(String id, CmsTemplate cmsTemplate) {
        //根据id从数据库查询页面信息
        CmsTemplate one = this.findById(id).getCmsTemplate();
        if(one!=null){
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            one.setTemplateId(cmsTemplate.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsTemplate.getSiteId());
            // 更新模板名称
            one.setTemplateName(cmsTemplate.getTemplateName());
            // 更新模板参数
            one.setTemplateParameter(cmsTemplate.getTemplateParameter());
            // 删除原有的模板文件
            if (!cmsTemplate.getTemplateFileId().equals(one.getTemplateFileId())){
                 gridFsTemplate.delete(Query.query(Criteria.where("_id").is(one.getTemplateFileId())));
            }
            // 更新文件id
            one.setTemplateFileId(cmsTemplate.getTemplateFileId());
            //提交修改
            cmsTemplateRepository.save(one);
            return new CmsTemplateResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsTemplateResult(CommonCode.FAIL,null);
    }
}

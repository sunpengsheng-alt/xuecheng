package com.czxy.xuecheng.manage_cms.dao;

import com.czxy.xuecheng.domain.cms.CmsTemplate;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

    //根据页面id查询页面信息
    @ApiOperation("根据模板id查询模板信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "唯一标识",required=true,paramType="path",dataType="int")
    })
    public Optional<CmsTemplate> findById(String id);


    CmsTemplate findBysiteIdAndTemplateNameAndTemplateParameterAndTemplateFileId(String siteId, String templateName, String templateParameter, String templateFileId);
}

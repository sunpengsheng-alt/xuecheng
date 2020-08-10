package com.czxy.xuecheng.api.cms;

import com.czxy.xuecheng.domain.cms.response.CmsTemplateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value="cms模板管理接口",description = "cms模板管理接口，提供模板的增、删、改、查")
public interface CmsTemplateControllerApi {

    @ApiOperation("模板文件上传")
    public CmsTemplateResult upload(MultipartFile file);

}

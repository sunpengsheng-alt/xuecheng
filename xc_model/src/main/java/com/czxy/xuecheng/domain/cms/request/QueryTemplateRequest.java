package com.czxy.xuecheng.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class QueryTemplateRequest {

    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;
    //模版ID
    @Id
    @ApiModelProperty("模版ID")
    private String templateId;
    //模版名称
    @ApiModelProperty("模版名称")
    private String templateName;
    //模版参数
    @ApiModelProperty("模版参数")
    private String templateParameter;

    //模版文件Id
    @ApiModelProperty("模版文件Id")
    private String templateFileId;

}

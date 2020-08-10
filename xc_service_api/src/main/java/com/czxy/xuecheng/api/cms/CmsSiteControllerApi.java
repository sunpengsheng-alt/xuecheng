package com.czxy.xuecheng.api.cms;

import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.domain.cms.CmsSite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "cms站点管理接口", description = "提供站点的增、删、改、查操作")
public interface CmsSiteControllerApi {

    @ApiOperation("查询所有")
    public QueryResponseResult<CmsSite> findAll();

}

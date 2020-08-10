package com.czxy.xuecheng.api.System;

import com.czxy.xuecheng.domain.system.response.SysDictionaryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "数据字典接口",description = "数据字典接口,进行数据字典的查询")
public interface SysDictionaryControllerApi {

    @ApiOperation("通常类型查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dType",value = "类型",
            required = true,paramType = "path",dataType = "string"),
    })
    public SysDictionaryResult findByType(String dType);
}

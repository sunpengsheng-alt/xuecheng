package com.czxy.xuecheng.swagger.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "user对象",description = "user描述信息")
public class User {
    @ApiModelProperty(value="唯一标识",required = true)
    private Integer uid;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty(value="密码")
    private String password;
}



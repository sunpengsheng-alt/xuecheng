package com.czxy.xuecheng.swagger.controller;

import com.czxy.xuecheng.swagger.domain.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/demo")
@Api(tags = "入门程序接口",description = "这个Swagger入门程序的接口，提供基本CRUD操作")
public class DemoController {

    @GetMapping("/findAll")
    @ApiOperation("查询所有")
    public List findById(){
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("123");
        return list;
    }

    @GetMapping("/{id}")
    @ApiOperation("通过id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="唯一编号",required = true,paramType = "path",dataType = "String")
    })
    public String findById(@PathVariable String id){
        return "查询成功" + id;
    }

    @PostMapping
    @ApiOperation("添加操作")
    @ApiResponses({
            @ApiResponse(code = 400,message = "参数异常"),
            @ApiResponse(code = 500,message = "服务器端异常"),
            @ApiResponse(code = 20000,message = "自定义状态码异常")
    })
    public User save(@RequestBody User user){
        return user;
    }

}

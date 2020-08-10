package com.czxy.xuecheng.manage_cms.controller;

import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.domain.cms.CmsSite;
import com.czxy.xuecheng.manage_cms.service.PageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cms/site")
public class CmsSiteController {

    @Resource
    private PageService pageService;

    /**
     * 查询所有站点
     * @return
     */
    @GetMapping
    public QueryResponseResult<CmsSite> findAll(){
        return pageService.findAllSite();
    }

}

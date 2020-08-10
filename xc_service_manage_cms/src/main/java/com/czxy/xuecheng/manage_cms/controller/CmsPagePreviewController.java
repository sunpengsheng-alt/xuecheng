package com.czxy.xuecheng.manage_cms.controller;

import com.czxy.xuecheng.common.web.BaseController;
import com.czxy.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/cms/preview")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    /**
     * 接收到页面id
     */
    @GetMapping(value = "/{pageId}")
    public void preview(@PathVariable("pageId")String pageId){
        String pageHtml = pageService.getPageHtml(pageId);
        if (StringUtils.isNotEmpty(pageHtml)){
            try {
                response.setContentType("text/html;charset=utf-8");
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

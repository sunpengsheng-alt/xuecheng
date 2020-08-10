package com.czxy.xuecheng;

import com.czxy.xuecheng.domain.course.ext.CourseView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class TestFreeMarker2 {
    @Test
    public void testGenerateHtml() throws Exception {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());

        // 设置模板路径
        String classpath = this.getClass().getResource("/").getPath();
        File templatesDir = new File(classpath, "/templates/");
        configuration.setDirectoryForTemplateLoading(templatesDir);

        //设置字符集
        configuration.setDefaultEncoding("UTF-8");

        //加载模板
        Template template = configuration.getTemplate("course.ftl");

        //模型数据
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        String dataUrl = "http://localhost:31200/course/courseview/4028e581617f945f01617f9dabc40000";
        ResponseEntity<CourseView> entity = restTemplate.getForEntity(dataUrl, CourseView.class);
        CourseView courseView = entity.getBody();

        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, courseView);

        //输出
        File file = new File(templatesDir , "course.html");
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile( file ,content);
    }

}


package com.czxy.xuecheng;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TestFreeMarker {
    @Test
    public void  testGenerateHtml() throws IOException, TemplateException {
        // 创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 设置模板路径
        String classpath = this.getClass().getResource("/").getPath();
        File templatesDir = new File(classpath, "/templates/");
        configuration.setDirectoryForTemplateLoading(templatesDir);
        // 设置字符集
        configuration.setDefaultEncoding("UTF-8");
        // 加载模板
        Template template = configuration.getTemplate("test.ftl");
        // 模型数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","程序猿和程序媛的故事");
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        // 输出
        File file = new File(templatesDir , "test.html");
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile(file,content);
    }

    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        // 模板字符串
        String templatesString = "<html>" +
                "    <head>" +
                "        <meta charset=\"UTF-8\">" +
                "        <title>标题</title>" +
                "    </head>" +
                "    <body>" +
                "        ${name}" +
                "    </body>" +
                "</html>";
        // 模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("myTemplate",templatesString);
        // 创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获得模板
        Template template = configuration.getTemplate("myTemplate", "UTF-8");
        // 模型数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","程序猿和程序员的故事");
        // 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        // 输出
        String classpath = this.getClass().getResource("/").getPath();
        File templatesDir = new File(classpath, "/templates/");
        File file = new File(templatesDir, "test2.html");
        System.out.println(file.getAbsolutePath());
        FileUtils.writeStringToFile(file,content);
    }
}

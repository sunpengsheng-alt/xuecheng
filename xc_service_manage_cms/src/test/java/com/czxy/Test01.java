package com.czxy;

import com.czxy.xuecheng.domain.cms.CmsConfig;
import com.czxy.xuecheng.domain.cms.CmsConfigModel;
import com.czxy.xuecheng.manage_cms.ManageCmsApplication;
import com.czxy.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageCmsApplication.class)
public class Test01 {

    @Resource
    private CmsConfigRepository cmsConfigRepository;

    @Test
    public void run1(){
        CmsConfig cmsConfig = new CmsConfig();
        cmsConfig.setName("测试数据");
        ArrayList<CmsConfigModel> cmsConfigModels = new ArrayList<>();
        CmsConfigModel cmsConfigModel = new CmsConfigModel();
        cmsConfigModel.setKey("id");
        cmsConfigModel.setValue("123");
        cmsConfigModels.add(cmsConfigModel);
        cmsConfig.setModel(cmsConfigModels);
        cmsConfigRepository.insert(cmsConfig);
    }
}

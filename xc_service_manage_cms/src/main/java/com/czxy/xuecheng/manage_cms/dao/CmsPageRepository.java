package com.czxy.xuecheng.manage_cms.dao;

import com.czxy.xuecheng.domain.cms.CmsPage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称、站点Id、页面webpath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);

    //根据页面id查询页面信息
    @ApiOperation("根据页面id查询页面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "唯一标识",required=true,paramType="path",dataType="int")
    })
    public Optional<CmsPage> findById(String id);

}

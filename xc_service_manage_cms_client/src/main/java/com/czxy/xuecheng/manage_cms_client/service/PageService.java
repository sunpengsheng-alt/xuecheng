package com.czxy.xuecheng.manage_cms_client.service;

import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.domain.cms.CmsPage;
import com.czxy.xuecheng.domain.cms.CmsSite;
import com.czxy.xuecheng.domain.cms.response.CmsCode;
import com.czxy.xuecheng.domain.cms.response.CmsPageResult;
import com.czxy.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.czxy.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.FileUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


@Service
public class PageService {
    @Resource
    private CmsPageRepository cmsPageRepository;

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    @Resource
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 将页面html保存到页面物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId){
        try {
            //1 html的文件id
            // 1.1 查询页面
            CmsPage cmsPage = findPageById(pageId).getCmsPage();

            // 1.2 获得文件id
            String htmlFileId = cmsPage.getHtmlFileId();

            //2 从gridFS获得文件流
            InputStream inputStream = this.getFileById(htmlFileId);

            //3 拼凑物理路径,页面物理路径=站点物理路径+页面物理路径+页面名称
            // 3.1 得到站点id
            String siteId = cmsPage.getSiteId();
            // 3.2 得到站点的信息
            CmsSite cmsSite = this.findCmsSiteById(siteId);
            // 3.3 得到站点的物理路径
            String sitePhysicalPath = cmsSite.getSitePhysicalPath();
            // 3.4 得到页面的物理路径
            String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

            //4 保存文件
            FileUtils.copyInputStreamToFile(inputStream,new File(pagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询站点详情
     * @param id
     * @return
     */
    public CmsSite findCmsSiteById(String id){
        Optional<CmsSite> optional = cmsSiteRepository.findById(id);
        if(optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
        return null;
    }


    /**
     * 通过文件id从gridFS获得文件流
     * @param fileId
     * @return
     */
    private InputStream getFileById(String fileId) {
        try {
            //文件对象
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            //打开下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //定义GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询页面详情
     * @param id
     * @return
     */
    public CmsPageResult findPageById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CmsCode.CMS_PAGE_NOTEXISTS,null);
    }

}

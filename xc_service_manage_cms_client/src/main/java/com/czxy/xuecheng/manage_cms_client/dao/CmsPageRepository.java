package com.czxy.xuecheng.manage_cms_client.dao;

import com.czxy.xuecheng.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}

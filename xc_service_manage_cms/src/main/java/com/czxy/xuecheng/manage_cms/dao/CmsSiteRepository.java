package com.czxy.xuecheng.manage_cms.dao;

import com.czxy.xuecheng.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}

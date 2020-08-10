package com.czxy.xuecheng.manage_cms.dao;

import com.czxy.xuecheng.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDicionaryRepository extends MongoRepository<SysDictionary,String> {

    /**
     *  通过类型查询
     * @param dType
     * @return
     */
    public SysDictionary findByDType(String dType);
}

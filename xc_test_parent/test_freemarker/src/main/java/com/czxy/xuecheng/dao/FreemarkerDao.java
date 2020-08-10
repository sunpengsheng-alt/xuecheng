package com.czxy.xuecheng.dao;

import com.czxy.xuecheng.domain.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FreemarkerDao extends MongoRepository<Teacher,String> {

}

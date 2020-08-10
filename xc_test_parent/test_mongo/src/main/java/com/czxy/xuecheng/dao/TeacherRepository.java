package com.czxy.xuecheng.dao;

import com.czxy.xuecheng.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher,String> {

    //根据名称查询
    public Teacher findByName(String name);

    //根据名称模糊查询
    public List<Teacher> findByNameLike(String name);

    //根据名称和年龄查询
    public List<Teacher> findByNameLikeAndAge(String name, Integer age);

    //根据名称和年龄，分页查询
    public Page<Teacher> findByNameLikeAndAge(String name, Integer age, Pageable pageable);

}

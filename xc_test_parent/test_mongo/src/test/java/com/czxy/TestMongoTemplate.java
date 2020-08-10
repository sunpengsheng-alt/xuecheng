package com.czxy;


import com.czxy.xuecheng.TestApplication;
import com.czxy.xuecheng.domain.Teacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TestMongoTemplate {

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void  testFind(){
        // 等值查询
        // 创建查询条件对象 Query
        Query query = new Query();
        // 设置等值查询条件   where age = 21
        query.addCriteria(Criteria.where("age").is(20));
        // 查询
        List<Teacher> teachers = mongoTemplate.find(query, Teacher.class);
        /// 处理数据
        teachers.forEach(System.out::println);
    }

    @Test
    public void testFind2(){
        // gte 大于等于，gt 大于，lte小于等于，lt小于
        //查询 age 大于等于15
        Query query = new Query();
        query.addCriteria( Criteria.where("age").gte(15) );
        List<Teacher> list = mongoTemplate.find(query, Teacher.class);
        for (Teacher teacher : list) {
            System.out.println(teacher);
        }
    }


    @Test
    public void testFind3(){
        // 分页条件
        int pageNum = 0;
        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        // 查询条件
        // 查询条件对象
        Query query = new Query();
        // 拼凑查询条件
        query.addCriteria(Criteria.where("age").is(20));
        // 查询总条数
        long count = mongoTemplate.count(query, Teacher.class);
        System.out.println(count);
        // 添加查询条件
        query.with(pageRequest);
        // 查询
        List<Teacher> list = mongoTemplate.find(query, Teacher.class);
        // 处理结果
        list.forEach(System.out::println);
    }

    @Test
    public void testFind4(){
        // 分页 + 排序

        // 排序
        Sort sort = Sort.by(Sort.Order.asc("age"));

        // 分页条件
        int pageNum = 0;
        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        // 查询条件
        // 查询条件对象
        Query query = new Query();
        // 拼凑查询条件
        query.addCriteria(Criteria.where("password").is(1234));
        // 查询总条数
        long count = mongoTemplate.count(query, Teacher.class);
        System.out.println(count);
        // 添加查询条件
        query.with(pageRequest);
        // 查询
        List<Teacher> list = mongoTemplate.find(query, Teacher.class);
        // 处理结果
        list.forEach(System.out::println);
    }

    @Test
    public void testFind5(){
        String str = "孙";
        // 查询条件
        // 查询条件对象
        Query query = new Query();
        // 添加模糊查询
        Pattern compile = Pattern.compile("^.*" + str + ".*", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("name").regex(compile));
        // 查询
        List<Teacher> list = mongoTemplate.find(query, Teacher.class);
        // 处理结果
        list.forEach(System.out::println);
    }
}

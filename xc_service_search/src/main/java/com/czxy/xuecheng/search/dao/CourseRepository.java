package com.czxy.xuecheng.search.dao;

import com.czxy.xuecheng.domain.search.Course;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseRepository extends ElasticsearchRepository<Course,String> {
}

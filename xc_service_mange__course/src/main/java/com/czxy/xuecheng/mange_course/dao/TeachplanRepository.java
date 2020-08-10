package com.czxy.xuecheng.mange_course.dao;

import com.czxy.xuecheng.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    /**
     * 查询指定parentId的课程计划
     * @param courseId
     * @param parentId
     * @return
     */
    public List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}

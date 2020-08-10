package com.czxy.xuecheng.search.service;

import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.common.model.response.QueryResponseResult;
import com.czxy.xuecheng.common.model.response.QueryResult;
import com.czxy.xuecheng.domain.search.Course;
import com.czxy.xuecheng.domain.search.CourseSearchParam;
import com.czxy.xuecheng.search.dao.CourseRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseService {

    @Resource
    private CourseRepository coursePubRepository;

    public QueryResponseResult<Course> list(int page, int size, CourseSearchParam courseSearchParam) {
        // 1. 创建查询构建器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 2. 复杂查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        // 2.1 分页
        queryBuilder.withPageable(PageRequest.of(page-1,size));
        // 3. 查询,获取结果
        Page<Course> search = coursePubRepository.search(queryBuilder.build());
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(search.getTotalElements());
        queryResult.setList(search.getContent());
        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }
}

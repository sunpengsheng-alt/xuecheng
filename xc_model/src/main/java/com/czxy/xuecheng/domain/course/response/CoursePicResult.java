package com.czxy.xuecheng.domain.course.response;

import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.common.model.response.ResultCode;
import com.czxy.xuecheng.domain.course.CoursePic;
import lombok.Data;

@Data
public class CoursePicResult extends ResponseResult {

    private CoursePic coursePic;

    public CoursePicResult(ResultCode resultCode,CoursePic coursePic){
        super(resultCode);
        this.coursePic = coursePic;
    }
}

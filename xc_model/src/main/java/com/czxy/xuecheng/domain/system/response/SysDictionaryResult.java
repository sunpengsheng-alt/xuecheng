package com.czxy.xuecheng.domain.system.response;

import com.czxy.xuecheng.common.model.response.ResponseResult;
import com.czxy.xuecheng.common.model.response.ResultCode;
import com.czxy.xuecheng.domain.system.SysDictionary;
import lombok.Data;

@Data
public class SysDictionaryResult extends ResponseResult {
    private SysDictionary sysDictionary;
    public SysDictionaryResult(ResultCode resultCode,SysDictionary sysDictionary) {
        super(resultCode);
        this.sysDictionary = sysDictionary;
    }
}

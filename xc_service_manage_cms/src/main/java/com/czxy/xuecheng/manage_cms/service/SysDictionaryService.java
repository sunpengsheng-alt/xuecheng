package com.czxy.xuecheng.manage_cms.service;

import com.czxy.xuecheng.common.model.response.CommonCode;
import com.czxy.xuecheng.domain.system.SysDictionary;
import com.czxy.xuecheng.domain.system.response.SysDictionaryResult;
import com.czxy.xuecheng.manage_cms.dao.SysDicionaryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysDictionaryService {

    @Resource
    private SysDicionaryRepository sysDicionaryRepository;

    public SysDictionaryResult findByType(String dType){
        SysDictionary sysDictionary = sysDicionaryRepository.findByDType(dType);
        return new SysDictionaryResult(CommonCode.SUCCESS,sysDictionary);
    }
}

package com.czxy.xuecheng.guava;


import com.google.common.collect.ImmutableBiMap;

public class TestMap {
    public static void main(String[] args) {
        // 1. 创建构造器不可变Map
        ImmutableBiMap.Builder<String, String> mapbuilder = new ImmutableBiMap.Builder<>();

        // 2. 添加数据
        mapbuilder.put("k1","v1");
        mapbuilder.put("k2","v2");
        mapbuilder.put("k3","v3");

        // 3.转换趋同Mao(不可变的)
        ImmutableBiMap<String, String> map = mapbuilder.build();

        // 4.获得值
        String value = map.get("k1");
        System.out.println(value);

        // 5.不可以添加,将抛异常 UnsupportedOperationException 不支持操作异常
       // map.put("","");
    }


}

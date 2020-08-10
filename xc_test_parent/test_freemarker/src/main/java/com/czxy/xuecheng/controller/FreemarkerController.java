package com.czxy.xuecheng.controller;

import com.alibaba.fastjson.JSONObject;
import com.czxy.xuecheng.dao.FreemarkerDao;
import com.czxy.xuecheng.domain.Teacher;
import com.czxy.xuecheng.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class FreemarkerController {

    @Resource
    private FreemarkerDao freemarkerDao;

    @GetMapping("/testpath")
    public String testpath(){
        return "test";
    }

    @GetMapping("/model_a")
    public ModelAndView model_a(){
        ModelAndView modelAndView = new ModelAndView();
        //设置模型（数据）
        Map<String,String> map = new HashMap<>();
        map.put("name","jack");
        map.put("age","18");
        modelAndView.addObject("user" , map);
        //设置视图（页面）
        modelAndView.setViewName("model_a");
        return modelAndView;
    }

    @GetMapping("/model_b.ftl")
    public String model_b(Model model){
        //设置模型（数据）
        Map<String,String> map = new HashMap<>();
        map.put("name","jack");
        map.put("age","19");
        model.addAttribute("user",map);
        //设置视图（页面）
        return "model_b";
    }

    @GetMapping("/model_c.ftl")
    public String model_c(Map map){
        Map<String,String> userMap = new HashMap<>();
        userMap.put("name","jack");
        userMap.put("age","20");
        map.put("user",userMap);
        return "model_c";
    }

    @GetMapping("/list")
    public String list(Model model){
        //设置模型（数据）
        List<User> list = new ArrayList();
        list.add(new User("jack","1234",18));
        list.add(new User("rose","5678",21));
        model.addAttribute("users", list);
        return "list";
    }

    @GetMapping("/map")
    public String map(Model model){

        Map<String,User> map = new HashMap<>();
        map.put("user1", new User("jack","1234",18));
        map.put("user2", new User("rose","5678",21));
        model.addAttribute("allUser", map);
        //设置视图（页面）
        return "map";
    }

    @GetMapping("/if")
    public String _if(Model model){

        //设置模型（数据）
        model.addAttribute("token", 1234);
        model.addAttribute("token2", "1234");

        //设置视图（页面）
        return "if";
    }

    @GetMapping("/model_teacher.ftl")
    public String model_teacher(Model model){
        // 设置模型数据
        List<Teacher> list = freemarkerDao.findAll();
        System.out.println(list);
        model.addAttribute("teacherList",list);
        // 设置视图
        return "model_teacher";
    }

    @GetMapping("/method")
    public String method(Model model){
        // 设置模型(数据)
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");
        list.add("123");
        model.addAttribute("list",list);
        model.addAttribute("birthday",new Date());
        model.addAttribute("money",4541354);
        model.addAttribute("text", JSONObject.toJSONString(new User("加加加","12345",21)));
        return "method";

    }
}

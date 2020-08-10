package com.czxy;

import com.czxy.xuecheng.TestApplication;
import com.czxy.xuecheng.dao.TeacherRepository;
import com.czxy.xuecheng.domain.Teacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TestRepository {
    @Resource
    private TeacherRepository teacherRepository;

    /**
     * 查询所有
     */
    @Test
    public void testFindAll(){
        List<Teacher> list = teacherRepository.findAll();
        System.out.println(list);
    }

    /**
     * 添加
     */
    @Test
    public void testInsert(){
        Teacher teacher = new Teacher();
        teacher.setName("孙老师");
        teacher.setAge(25);
        teacher.setMarry("卫东");
        teacherRepository.insert(teacher);
    }

    /**
     * 更新
     */
    @Test
    public void testUpdate(){
        Optional<Teacher> optional = teacherRepository.findById("5ede4bf43335a82d386f49ff");
        if(optional.isPresent()){
            Teacher teacher = optional.get();
            teacher.setName("CC老师");
            teacherRepository.save(teacher);
        }
    }

    /**
     * 删除
     */
    @Test
    public void testDelete(){
        teacherRepository.deleteById("5ede57fd3335a8258875e0f2");
    }

    /**
     * 分页
     */
    @Test
    public void testPage(){
        int page = 0; //从0开始
        int size = 2;//每页记录数
        PageRequest pageRequest = PageRequest.of(page, size);
        //查询
        Page<Teacher> all = teacherRepository.findAll(pageRequest);
        // 总条数
        System.out.println(all.getTotalElements());
        // 当前页信息
        all.forEach(teacher -> {
            System.out.println(teacher);
        });
        // 将stream转换成List
        List<Teacher> list = all.get().collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void testDao(){
        Teacher teacher = teacherRepository.findByName("CC老师");
        System.out.println(teacher);

        List<Teacher> list = teacherRepository.findByNameLike("老师");
        System.out.println(list);

        List<Teacher> list2 = teacherRepository.findByNameLikeAndAge("老师", 18);
        System.out.println(list2);

        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Teacher> page = teacherRepository.findByNameLikeAndAge("老师", 18, pageRequest);
        List<Teacher> list3 = page.get().collect(Collectors.toList());
        System.out.println(list3);

    }




}

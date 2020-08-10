package com.czxy.xuecheng.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "teacher")
public class Teacher {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String marry;
}

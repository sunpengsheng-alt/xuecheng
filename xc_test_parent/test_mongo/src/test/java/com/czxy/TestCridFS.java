package com.czxy;

import com.czxy.xuecheng.TestApplication;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TestCridFS {

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Test
    public void testStoore() throws FileNotFoundException {
        // 要存储的文件
        File file = new File("F:/sps/index.html");
        // 定义输入流
        FileInputStream inputStream = new FileInputStream(file);
        // 向GridFs存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "首页测试00001","");
        // 得到文件ID
        System.out.println(objectId);
    }

    @Test
    public void testFind() {
        String id = "5f05c7adfdb5401aa8be4e40";
        Query query = Query.query(Criteria.where("_id").is(id));
        GridFSFile gridFSFile = gridFsTemplate.find(query).first();
        System.out.println(gridFSFile.getFilename());
    }

    @Test
    public void testFindAll() {
        GridFsResource[] resources = gridFsTemplate.getResources("*");
        for (GridFsResource resource : resources) {
            System.out.println(resource.getFilename());
        }
    }

    @Test
    public void testDownload() throws IOException {
        // 创建mongodb客户端
        MongoClientURI connectionString = new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        // 获得数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        // 根据数据库获得bucket
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        // 查询
        String id = "5f0439ed9547c137c020af21";
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        // 打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 创建GridFsResource对象,获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        // 获得流,并操作
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);
    }
}

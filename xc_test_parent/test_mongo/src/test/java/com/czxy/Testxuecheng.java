package com.czxy;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;


public class Testxuecheng {
    /**
     * 连接本地数据库
     */
    @Test
    public void testConnection() {
        //创建mongodb 客户端
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        System.out.println(mongoClient);
    }

    /**
     *通过字符串获得连接
     */
    @Test
    public void testConnection2() {
        //采用连接字符串
        MongoClientURI connectionString = new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        System.out.println(mongoClient);
    }

    /**
     * 通过字符串获得连接并查询第一个文档
     */
    @Test
    public void testFind(){
        //采用连接字符串
        MongoClientURI connectionString = new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        // 连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        // 连接collection
        MongoCollection<Document> collection = database.getCollection("student");
        //查询第一个文档
        Document myDoc = collection.find().first();
        //得到文件内容 json串
        String json = myDoc.toJson();
        System.out.println(json);
    }

    /**
     * 创建集合
     */
    @Test
    public void testCollection(){
        // 采用连接字符串
        MongoClientURI connectionString = new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        // 连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        // 创建集合
        database.createCollection("teacher");
    }

    /**
     * 插入一个文档
     */
    @Test
    public  void  testDocument(){
        //采用字符串连接
        MongoClientURI clientURI= new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        //获得集合
        MongoCollection<Document> collection = database.getCollection("teacher");
        //插入文档
        Document document = new Document();
        document.append("name","呵呵");
        document.append("age",20);
        document.append("marry","new...");
        collection.insertOne(document);
    }
    /**
     * 批量插入文档
     */
    @Test
    public  void  testDocument2(){
        //采用字符串连接
        MongoClientURI clientURI= new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        //获得集合
        MongoCollection<Document> collection = database.getCollection("teacher");
        //插入文档
        Document d1 = new Document();
        d1.append("name","嘻嘻");
        d1.append("age",19);
        d1.append("marry","new...");
        Document d2 = new Document();
        d2.append("name","哈哈");
        d2.append("age",18);
        d2.append("marry","new...");
        //创建ArrayList,把文档存到集合中
        ArrayList<Document> document = new ArrayList<>();
        document.add(d1);
        document.add(d2);
        collection.insertMany(document);
    }

    /**
     * 查询所有
     */
    @Test
    public  void  testDocumentAll(){
        //采用字符串连接
        MongoClientURI clientURI= new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        //获得集合
        MongoCollection<Document> collection = database.getCollection("teacher");
        //查询所有
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> iterator = findIterable.iterator();
        while (iterator.hasNext()){
            Document document = iterator.next();
            String name = document.get("name", String.class);
            Integer age = document.get("age", Integer.class);
            String marry = document.get("marry", String.class);
            System.out.println(name+"-"+age+"-"+marry);
        }
    }

    /**
     * 更新文档
     */
    @Test
    public  void  testDocumentUpdate() {
        //采用字符串连接
        MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@localhost:27017");
        MongoClient mongoClient = new MongoClient(clientURI);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        //获得集合
        MongoCollection<Document> collection = database.getCollection("teacher");
        //更新文档
        collection.updateOne(Filters.eq("age",20),new Document("$set",new Document("name","123321132")));
    }


    /**
     * 删除文档
     */
    @Test
    public  void  testDocumentdelete() {
        //采用字符串连接
        MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@localhost:27017/demo");
        MongoClient mongoClient = new MongoClient(clientURI);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("demo2");
        //获得集合
        MongoCollection<Document> collection = database.getCollection("teacher");
        //删除文档
        collection.deleteOne(Filters.eq("age", 20));
    }


}

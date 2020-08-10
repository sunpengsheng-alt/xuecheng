package com.czxy.xuecheng;

import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastfds {

    @Test
    public void testUpload() {
        try {
            // 加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            // 定义TrackerClient,用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接trackerClient
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            // 向storage服务器上传文件
            // 本地文件的路径
            String filePath = "G:/other/img/0003.png";
            // 上传成功后拿到文件id
            String fileId = storageClient1.upload_appender_file1(filePath,"png",null);
            System.out.println(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ronghui.service.service.impl;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.ronghui.service.service.MongoDBService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class MongoDBServiceImpl implements MongoDBService {
    @Value("${spring.data.mongodb.uri}")
    String mongoUri = "mongodb://root:123@39.108.106.167:27017/admin";
    @Value("${spring.data.mongodb.port}")
    String port = "27017";
    @Value("${spring.data.mongodb.host}")
    String host = "39.108.106.167";
    @Value("${spring.data.mongodb.username}")
    String username = "root";
    @Value("${spring.data.mongodb.password}")
    String password = "123";
    @Value("${spring.data.mongodb.database}")
    String dbName = "gridfs";


    private static MongoInstance mongoInstance;

    private static final class MongoInstance {
        MongoClient client;

        MongoInstance(String mongoUri) {
            MongoClientSettings settings = MongoClientSettings
                    .builder()
                    .applyConnectionString(new ConnectionString(mongoUri))
                    .applyToConnectionPoolSettings(builder -> builder.maxSize(25)
                            .minSize(10)
                            .maxWaitTime(25, TimeUnit.SECONDS)
                            .maxConnectionIdleTime(10000, TimeUnit.SECONDS)
                            .maxConnectionLifeTime(10000, TimeUnit.SECONDS)
                            .maintenanceInitialDelay(0, TimeUnit.SECONDS)
                            .build())
                    .build();
            client = MongoClients.create(settings);
        }
    }

    private MongoDatabase getDatabase() {
        if (mongoInstance == null) {
            synchronized (MongoInstance.class) {
                if (mongoInstance == null) {
                    mongoUri = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/admin";
                    mongoInstance = new MongoInstance(mongoUri);
                }
            }
        }
        return mongoInstance.client.getDatabase(dbName);
    }


    /**
     * 用给出的id，保存文件，透明处理已存在的情况
     * id 可以是string，long，int，org.bson.types.ObjectId 类型
     *
     * @param in
     * @param id
     */
    @Override
    public String save(InputStream in, Object id, String fileName) {
        GridFSBucket bucket = GridFSBuckets.create(getDatabase());
        GridFSUploadOptions options = new GridFSUploadOptions();
        ObjectId objectId = bucket.uploadFromStream(fileName, in, options);
        log.info("save：{}\nid:{} ", fileName, objectId.toHexString());
        return objectId.toHexString();
    }

    /**
     * 据id返回文件
     *
     * @param id
     * @return
     */
    @Override
    public byte[] getById(String id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GridFSBucket bucket = GridFSBuckets.create(getDatabase());
        byte[] data = null;
        try {
            bucket.downloadToStream(new ObjectId(id), outputStream);
            data = outputStream.toByteArray();
        } catch (MongoGridFSException e) {
            log.error("getByFileName", e);

        } finally {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
        return data;
    }

    /**
     * 据文件名返回文件，只返回第一个
     *
     * @param fileName
     * @return
     */
    @Override
    public byte[] getByFileName(String fileName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MongoDatabase mongoDatabase = getDatabase();
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase);
        byte[] data = null;
        try {
            bucket.downloadToStream(fileName, outputStream);
            data = outputStream.toByteArray();

        } catch (MongoGridFSException e) {
            log.error("getByFileName", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
        return data;
    }
}

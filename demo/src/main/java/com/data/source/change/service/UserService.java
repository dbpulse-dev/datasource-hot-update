package com.data.source.change.service;

import com.data.source.change.entity.UserEntity;
import com.data.source.change.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init() {
        queries();
    }

    private void queries() {
        new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                final long l = System.currentTimeMillis();
                List<UserEntity> users = null;
                try {
                    users = userMapper.listAll();
                } catch (Throwable t) {
                    logger.error("获取数据失败:", t);
                } finally {
                    if (users == null) {
                        logger.info("获取数据耗时:{}", (System.currentTimeMillis() - l));
                    } else {
                        logger.info("获取数据成功{}条数据,耗时{}ms", users.size(), (System.currentTimeMillis() - l));
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

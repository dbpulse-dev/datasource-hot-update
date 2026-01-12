package com.data.source.change.controller;


import com.data.source.change.RestResult;
import com.data.source.change.entity.UserEntity;
import com.data.source.change.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/user")

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserMapper userMapper;

    private AtomicInteger index = new AtomicInteger();

    @GetMapping("/add")
    public RestResult add() {
        UserEntity entity = new UserEntity();
        int i = index.incrementAndGet();
        entity.setName("test" + i);
        entity.setAge(i);
        userMapper.add(entity);
        return RestResult.buildSuccess();
    }

    @GetMapping("/list/all")
    public RestResult<List<UserEntity>> listAll() {
        List<UserEntity> entities = userMapper.listAll();
        return RestResult.buildSuccess(entities);
    }

    @GetMapping("/slow/query")
    public RestResult slowQuery() throws InterruptedException {
        float integer = selectTest(45);
        RestResult restResult = new RestResult();
        restResult.setData(integer);
        logger.info("获取mysql数据慢查数据");
        return restResult;
    }

    Random r = new Random();

    public float selectTest(int timesCost) throws InterruptedException {
        float s = (float) (r.nextInt(timesCost) + 5) / 10;
        userMapper.selectTest(s);
        return s;
    }

}

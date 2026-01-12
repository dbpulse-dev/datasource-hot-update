package com.data.source.change.controller;


import com.data.source.change.RestResult;
import com.data.source.hot.update.DataSourceConfigChangeListener;
import com.data.source.hot.update.DataSourceHotUpdateManager;

import com.data.source.hot.update.config.ConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/data-source")

public class DataSourceHotUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceHotUpdateController.class);

    @Autowired
    private DataSourceConfigChangeListener hotUpdateManager;

    @PostMapping("/update")
    public RestResult add(@RequestBody ConfigItem config) {
        hotUpdateManager.onConfigChange(Arrays.asList(config));
        return RestResult.buildSuccess();
    }

    AtomicInteger c = new AtomicInteger(0);

    @GetMapping("/refresh")
    public RestResult refresh() {
        ConfigItem config = new ConfigItem();
        if (c.getAndIncrement() % 2 == 0) {
            config.setOldValue("jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8");
            config.setNewValue("jdbc:mysql://localhost:3306/test_db2?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8");
        } else {
            config.setNewValue("jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8");
            config.setOldValue("jdbc:mysql://localhost:3306/test_db2?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8");
        }
        hotUpdateManager.onConfigChange(Arrays.asList(config));
        return RestResult.buildSuccess();
    }


}

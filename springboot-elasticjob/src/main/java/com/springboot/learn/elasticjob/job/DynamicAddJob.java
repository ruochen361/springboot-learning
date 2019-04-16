package com.springboot.learn.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时添加定时任务
 */

@Component
public class DynamicAddJob implements SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicAddJob.class);

    @Autowired
    private ElasticJobHandler handler;

    @Override
    public void execute(ShardingContext shardingContext) {
        LOGGER.info("动态添加定时任务开始");
        //定时任务以jobName为唯一值
        handler.addJob("Dynamic addition job1","0/30 * * * * ?",1,"add2","");
        handler.addJob("Dynamic addition job2","0/15 * * * * ?",1,"add2","");
        handler.addJob("Dynamic addition job2","0/15 * * * * ?",1,"add1","");
        LOGGER.info("动态添加定时任务结束");
    }
}

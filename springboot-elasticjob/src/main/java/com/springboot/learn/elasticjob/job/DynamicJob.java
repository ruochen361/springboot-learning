package com.springboot.learn.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.springboot.learn.elasticjob.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 被动态添加的定时任务
 *
 * 每次动态添加会新建对象
 * 因此该定时任务的属性，不能进行@Autowired注入，需进行有参构造
 */

public class DynamicJob implements SimpleJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicJob.class);

    private CommonService commonService;

    public DynamicJob() {
    }

    public DynamicJob(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public void execute(ShardingContext shardingContext) {
        LOGGER.info(String.format("当前时间:%s,当前任务名称:%s,当前任务参数:%s,当前分片数:%s,当前分片参数:%s,当前任务的id:%s",
                // 获取当前的时间
                System.currentTimeMillis(),
                // 获取当前的任务名称
                shardingContext.getJobName(),
                // 获取当前任务参数
                shardingContext.getJobParameter(),
                //获取当前任务分片数
                shardingContext.getShardingTotalCount(),
                //获取当前任务分片参数
                shardingContext.getShardingParameter(),
                // 获取任务的id
                shardingContext.getTaskId()
                ));
        commonService.test("测试变量注入");

    }
}

package com.springboot.learn.elasticjob.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import org.springframework.stereotype.Component;

/**
 * 监听器
 */
public class ElasticJobListener extends AbstractDistributeOnceElasticJobListener {

    /**
     * 设置间隔时间
     * @param startedTimeoutMilliseconds
     * @param completedTimeoutMilliseconds
     */
    public ElasticJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
         super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {

    }

    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
    }
}

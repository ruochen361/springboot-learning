package com.springboot.learn.elasticjob.job;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.springboot.learn.elasticjob.listener.ElasticJobListener;
import com.springboot.learn.elasticjob.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ElasticJobHandler {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Autowired
    private ElasticJobListener elasticJobListener;

    @Autowired
    private CommonService commonService;


    /**
     * 配置任务详细信息
     * @param jobClass 定时任务实现类
     * @param cron  表达式
     * @param shardingTotalCount 分片数
     * @param shardingItemParameters 分片参数
     * @param jobParameters 自定义参数
     * @return
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters,
                                                         final String jobParameters) {
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).
                shardingItemParameters(shardingItemParameters).jobParameter(jobParameters).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, jobClass.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;
    }

    /**
     * 方式一，读取配置文件初始化定时任务
     *
     * 初始化定时任务
     * @param dynamicAddJob 定时任务实现类
     * @param cron 表达式
     * @param shardingTotalCount 分片数
     * @param shardingItemParameters 分片参数
     * @param jobParameters 自定义参数
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final DynamicAddJob dynamicAddJob,
                                           @Value("${Job.cron.dynamicAddJob}") final String cron,
                                           @Value("${Job.shardingTotalCount.dynamicAddJob}") final int shardingTotalCount,
                                           @Value("${Job.shardingItemParameters.dynamicAddJob}") final String shardingItemParameters,
                                           @Value("${Job.jobParameters.dynamicAddJob}") final String jobParameters) {
        return new SpringJobScheduler(dynamicAddJob, zookeeperRegistryCenter, getLiteJobConfiguration(dynamicAddJob.getClass(),
                cron, shardingTotalCount, shardingItemParameters,jobParameters));
    }



    /**
     * 动态添加定时任务配置
     * @param jobName 任务名
     * @param jobClass 定时任务实现类
     * @param shardingTotalCount 分片数
     * @param cron 表达式
     * @param jobParameters   自定义参数
     * @return
     */
    private static LiteJobConfiguration.Builder simpleJobConfigBuilder(String jobName,
                                                                           Class<? extends SimpleJob> jobClass,
                                                                           int shardingTotalCount,
                                                                           String cron,
                                                                           String jobParameters,
                                                                           String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(
                        JobCoreConfiguration.newBuilder(jobName,cron,shardingTotalCount)
                                .shardingItemParameters(shardingItemParameters)
                                .jobParameter(jobParameters)
                                .build(),
                        jobClass.getCanonicalName()));
    }

     /**
      * 方式二，包装创建定时任务方法
      *
      * 动态添加一个DynamicJob定时任务
      *  若DynamicJob有属性，需进行有参构造
      * @param jobName            任务名
      * @param cron               表达式
      * @param shardingTotalCount 分片数
      * @param jobParameters   自定义参数
      * @param parameters         当前参数
      */
    public void addJob(String jobName, String cron, Integer shardingTotalCount, String jobParameters,String parameters) {
         LiteJobConfiguration jobConfig
                 = simpleJobConfigBuilder(jobName, DynamicJob.class,shardingTotalCount,cron,jobParameters,parameters).overwrite(true).build();

         new SpringJobScheduler(new DynamicJob(commonService), zookeeperRegistryCenter, jobConfig, elasticJobListener).init();
    }

}

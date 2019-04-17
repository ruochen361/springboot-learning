package com.springboot.learn.elasticjob.service.impl;

import com.springboot.learn.elasticjob.service.CommonService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
    @Override
    public void test(String param) {
        System.out.println("动态添加的定时任务正在执行。。。。。"+param);
    }
}

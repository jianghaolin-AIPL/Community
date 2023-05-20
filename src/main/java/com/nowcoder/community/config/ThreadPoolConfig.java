package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling  // 如果不加这个注解就说明定时任务没有启动
@EnableAsync
public class ThreadPoolConfig {
}

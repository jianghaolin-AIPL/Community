package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

// 配置 -> 数据库 -> 调用
@Configuration
public class QuartzConfig {

    // FactoryBean可简化Bean的实例化过程，因为有些Bean的实例化过程比较麻烦，有了FactoryBean，实例化就比较容易
    // JobDetailFactoryBean这个类底层封装了JobDetail详细实例化过程，对它做了简化
    // 1.通过FactoryBean封装了Bean的实例化过程
    // 2.将FactoryBean装配到Spring容器里
    // 3.将FactoryBean注入给其他的Bean
    // 4.该Bean得到的是FactoryBean所管理的对象实例

    // 配置JobDetail
//    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");  // 名字不能跟别的重复
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);  // 声明这个任务是长久保持，哪怕这个任务将来不在运行了，触发器都没有了，也存着
        factoryBean.setRequestsRecovery(true); // 任务是可恢复的，如果存在什么问题，任务可恢复
        return factoryBean;
    }

    // 配置Trigger(有两种选择：SimpleTriggerFactoryBean、CronTriggerFactoryBean, CronTriggerFactoryBean可表达负责的延迟逻辑)
//    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) { // 初始化Trigger的时候Trigger是依赖于JobDetail的
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000); // 每3000ms执行一次任务
        factoryBean.setJobDataMap(new JobDataMap()); // Trigger底层需要存储Job的一些状态，指定new JobDataMap()来存
        return factoryBean;
    }

    // 刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("PostScoreRefreshJob");  // 名字不能跟别的重复
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);  // 声明这个任务是长久保持，哪怕这个任务将来不在运行了，触发器都没有了，也存着
        factoryBean.setRequestsRecovery(true); // 任务是可恢复的，如果存在什么问题，任务可恢复
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) { // 初始化Trigger的时候Trigger是依赖于JobDetail的
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5); // 5分钟执行一次刷新
        factoryBean.setJobDataMap(new JobDataMap()); // Trigger底层需要存储Job的一些状态，指定new JobDataMap()来存
        return factoryBean;
    }
}

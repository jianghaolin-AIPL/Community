package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service // 业务组件加这个注解
//@Scope("prototype")  // 可以实现多例 默认不加是单例 很少用多例
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct  // 注解作用：这个方法会在构造器之后调用 （适合初始化操作）
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy // 销毁之前调用
    public void destory() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}

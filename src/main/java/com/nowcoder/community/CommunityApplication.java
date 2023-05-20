package com.nowcoder.community;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这是整个应用核心的入口配置类，这个配置类是最先被加载的
 */
@SpringBootApplication
public class CommunityApplication {

	@PostConstruct // 这个注解是用来管理bean的生命周期的，主要用来管理bean的初始化方法
	// 这个注解表示这是一个初始化方法，当容器实例化SensitiveFilter这个Bean以后，在调用构造器之后，会自动调用这个方法
	// 服务启动的时候Bean就会被实例化，所以服务器启动的时候就会初始化
	public void init() {
		// 解决netty启动冲突的问题
		// see Netty4Utils.setAvailableProcessors()
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}

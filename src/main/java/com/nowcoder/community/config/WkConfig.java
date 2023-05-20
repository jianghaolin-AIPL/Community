package com.nowcoder.community.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @PostConstruct  // 注解作用：这个方法会在构造器之后调用 （适合初始化操作）
    public void init() {
        // 创建WK图片目录
        File file = new File(wkImageStorage);
        if(!file.exists()) {
            file.mkdir();
            logger.info("创建WK图片目录：" + wkImageStorage);
        }
    }
}

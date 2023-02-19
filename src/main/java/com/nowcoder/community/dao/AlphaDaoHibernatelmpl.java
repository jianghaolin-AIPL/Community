package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")  // 数据库组件加这个注解，容器会自动扫描这个bean  括号里面的内容时给这个bean重新命名，容器可通过这个名加载这个bean
public class AlphaDaoHibernatelmpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}

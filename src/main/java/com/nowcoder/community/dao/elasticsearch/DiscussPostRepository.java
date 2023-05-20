package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// 这个接口是数据访问层的代码 Es可以被看成一个特殊的数据库
// @Mapper是Mybatis专用的注解 @Repository是Spring提供的数据访问层的注解
@Repository
// 继承的时候需要利用泛型说明这个接口处理的实体类型是谁:DiscussPost 另外还要声明实体类的主键是什么类型：Integer
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
    // ElasticsearchRepository这个父接口中已经定义好了对ES服务器访问的增删改查各种方法
}

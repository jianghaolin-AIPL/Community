package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode); // offset:起始行行号, limit:显示多少条

    int selectDiscussPostRows(@Param("userId") int userId);  // （1）@Param用于给参数起别名，因为有些参数名太长写起来麻烦
    // （2）如果在sql中需要动态的考虑某个条件，且在方法中有且仅有这一个条件，则必须加@Param取别名

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}

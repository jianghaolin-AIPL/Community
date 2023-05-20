package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * 实现Mapper
 * 方式一：在mapper目录下新建xml配置文件去写sql（之前采用的）
 * 方式二：在这个Mapper类中写注解去声明这个方法对应的sql
 */
@Mapper
@Deprecated // 声明一下这个组件不推荐使用了
public interface LoginTicketMapper {

    // 增删改的方法通常返回的都是整数（行数）
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 主键(id)自动生成
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    // 动态条件写法
//    @Update({
//            "<script>",
//            "update login_ticket set status=#{status} where ticket=#{ticket} ",
//            "<if test=\"ticket!=null\"> ",
//            "and 1=1 ",
//            "</if>",
//            "</script>"
//    })
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket, int status);
}

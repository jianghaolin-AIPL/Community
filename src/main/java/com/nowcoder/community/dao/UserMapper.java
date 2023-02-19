package com.nowcoder.community.dao;


import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

// 要想实现这个接口需要提供配置文件（在mapper文件夹下），配置文件里需要给每个方法配置所需要的sql，这样MyBatis底层会自动生成实现类
@Mapper // MyBatis的注解来标识bean，可以让容器自动扫描，和@Repository类似，当然这里用@Repository也行
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user); // 返回插入的行数

    int updateStatus(int id, int status);  // 返回修改了几行数据

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}

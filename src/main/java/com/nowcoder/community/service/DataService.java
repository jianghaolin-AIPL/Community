package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DataService {
    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    // 将指定的IP计入UV
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(sdf.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    // 统计指定范围内的UV
    public long calculateUV(Date start, Date end) {
        if(start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 整理该日期范围内的Key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getUVKey(sdf.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }

        // 合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(sdf.format(start), sdf.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey, keyList.toArray());

        // 返回统计结果
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    // 将指定用户计入DAU
    public void recordDAU(int userId) {
        String redisKey = RedisKeyUtil.getDAUKey(sdf.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    // 统计指定日期范围内的DAU
    public long calculateDAU(Date start, Date end) {
        if(start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 整理该日期范围内的Key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(sdf.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // 进行OR运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(sdf.format(start), sdf.format(end));
                connection.stringCommands().bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), keyList.toArray(new byte[0][0]));  // keyList.toArray(new byte[0][0]):转成二维的byte数组
                return connection.stringCommands().bitCount(redisKey.getBytes());
            }
        });
    }
    // 下面写表现层逻辑，分为两个方面：1.什么时候去记录这些值 2.怎么去查看
    // 1.记录这些值：每次请求都得记，每次请求都有可能是新的访问，在拦截器里写比较合适
    // 2.
}

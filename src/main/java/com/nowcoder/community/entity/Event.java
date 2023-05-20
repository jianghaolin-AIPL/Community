package com.nowcoder.community.entity;

import org.apache.kafka.common.protocol.types.Field;

import java.util.HashMap;
import java.util.Map;

public class Event {

    private String topic;
    private int userId; // 触发事件的人
    private int entityType; // 事件发生在哪个实体上
    private int entityId;
    private int entityUserId; // 实体作者
    // 事件具有通用性，现在只处理三种事件，以后可能会有更多的事件，在处理其他事件的时候可能还会有一些特殊的数据需要记录
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    // 当我们调用set方法的时候，set这个topic，还要set其他的属性；set完topic又返回当前对象，又可以调用当前对象其他set方法
    // 也可以写一个有参的构造器，一股脑全传进去，但是我们属性比较多，可能有些时候有些属性不需要传
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}

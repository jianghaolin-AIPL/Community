package com.nowcoder.community.event;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        // 除了主题，还要发一个字符串过去，这个字符串应该包含事件对象的所有数据，最好的方式是把event转换为json字符串，消费者得到这个字符串以后把它还原为event
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}

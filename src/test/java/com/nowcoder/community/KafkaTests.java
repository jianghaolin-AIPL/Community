package com.nowcoder.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka() {
        // 生产者发消息是主动的调，去发；消费者处理消息是被动的，一旦队列中有消息就自动处理
        kafkaProducer.sendMessage("test", "你好！"); // test这个主题之前通过命令行窗口创建好了
        kafkaProducer.sendMessage("test", "在吗？");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
// 生产者
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }
}

@Component
// 消费者
class KafkaConsumer {

    // 监听的是test这个主题
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }
}

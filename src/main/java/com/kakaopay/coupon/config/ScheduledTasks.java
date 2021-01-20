package com.kakaopay.coupon.config;

import com.kakaopay.coupon.domain.AlarmInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    private KafkaTemplate<String, AlarmInfo> kafkaTemplate;

    @Autowired
    public ScheduledTasks(KafkaTemplate<String, AlarmInfo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, AlarmInfo alarmInfo) {
        kafkaTemplate.send(topic, alarmInfo);
        log.info("Message: " + alarmInfo + " sent to topic: " + topic);
    }


    @KafkaListener(topics = "test")
    //public void receiveTopic1(ConsumerRecord consumerRecord) {
    public void receiveTopic1(String consumerRecord) {
        log.info("Receiver on topic1: "+consumerRecord.toString());
    }
}
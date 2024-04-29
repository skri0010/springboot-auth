package com.example.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "user-activity-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        logger.info("Message published to Kafka topic: " + TOPIC + " Message: " + message);
    }
}
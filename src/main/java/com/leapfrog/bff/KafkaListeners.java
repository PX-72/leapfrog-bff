package com.leapfrog.bff;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "${market-data.fx-prices-topic}", groupId = "${market-data.fx-prices-group}")
    void listener(String data) {

        System.out.println(MessageFormat.format("Received: {0}", data));
    }
}

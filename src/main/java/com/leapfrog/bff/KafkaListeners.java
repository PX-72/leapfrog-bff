package com.leapfrog.bff;

import com.leapfrog.bff.service.interfaces.MarketDataMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class KafkaListeners {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    private final MarketDataMessageProcessor marketDataMessageProcessor;

    @Autowired
    public KafkaListeners(MarketDataMessageProcessor marketDataMessageProcessor) {
        this.marketDataMessageProcessor = marketDataMessageProcessor;
    }

    @KafkaListener(topics = "${market-data.fx-prices-topic}", groupId = "${market-data.fx-prices-group}")
    void listener(String data) {
        logger.info(MessageFormat.format("Received: {0}", data));
        marketDataMessageProcessor.process(data);
    }
}

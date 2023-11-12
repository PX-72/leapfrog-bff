package com.leapfrog.bff.service.message.processors;

import com.leapfrog.bff.service.interfaces.MarketDataMessageProcessor;
import com.leapfrog.bff.service.interfaces.MarketDataWebSocketPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketDataMessageProcessorImpl implements MarketDataMessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataMessageProcessorImpl.class);

    private final MarketDataWebSocketPublisher marketDataWebSocketPublisher;


    @Autowired
    public MarketDataMessageProcessorImpl(MarketDataWebSocketPublisher marketDataWebSocketPublisher) {
        this.marketDataWebSocketPublisher = marketDataWebSocketPublisher;
    }


    @Override
    public void process(String message) {
        marketDataWebSocketPublisher.broadcast(message);
    }
}



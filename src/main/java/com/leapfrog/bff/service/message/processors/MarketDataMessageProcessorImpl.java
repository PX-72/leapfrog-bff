package com.leapfrog.bff.service.message.processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leapfrog.bff.models.ClientMarketDataEventType;
import com.leapfrog.bff.models.ClientMessageEvent;
import com.leapfrog.bff.models.ClientMessageType;
import com.leapfrog.bff.models.FxMarketData;
import com.leapfrog.bff.service.FxSubscriptionSessionMap;
import com.leapfrog.bff.service.interfaces.MarketDataMessageProcessor;
import com.leapfrog.bff.service.interfaces.WsPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketDataMessageProcessorImpl implements MarketDataMessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataMessageProcessorImpl.class);
    private static final Gson gson = new GsonBuilder().create();

    private final WsPublisher wsPublisher;
    private final FxSubscriptionSessionMap subscriptionSessionMap;

    @Autowired
    public MarketDataMessageProcessorImpl(WsPublisher wsPublisher,
                                          FxSubscriptionSessionMap subscriptionSessionMap) {
        this.wsPublisher = wsPublisher;
        this.subscriptionSessionMap = subscriptionSessionMap;
    }


    @Override
    public void process(String message) {
        try {
            var marketData = gson.fromJson(message, FxMarketData.class);
            var sessions = subscriptionSessionMap.getSessionIdsByCurrencyPair(marketData.ccyPair());

            var clientMessageType = ClientMarketDataEventType.MARKET_DATA_RESPONSE.getValue();
            var clientMessage = new ClientMessageEvent<>(clientMessageType, marketData);
            sessions.forEach(s -> wsPublisher.publish(gson.toJson(clientMessage), s));
        } catch (Throwable e) {
            logger.error("Error occurred while attempting to process market data input");
        }
    }
}



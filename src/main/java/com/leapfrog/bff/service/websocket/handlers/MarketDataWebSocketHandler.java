package com.leapfrog.bff.service.websocket.handlers;
import com.google.gson.reflect.TypeToken;
import com.leapfrog.bff.models.ClientMarketDataEventType;
import com.leapfrog.bff.models.ClientMessageEvent;
import com.leapfrog.bff.models.ClientMessageType;
import com.leapfrog.bff.service.FxSubscriptionSessionMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leapfrog.bff.service.websocket.WebSocketSessionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.lang.reflect.Type;

@Service
public class MarketDataWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataWebSocketHandler.class);
    private static final Gson gson = new GsonBuilder().create();

    private final WebSocketSessionContainer webSocketSessionContainer;
    private final FxSubscriptionSessionMap subscriptionSessionMap;

    @Autowired
    public MarketDataWebSocketHandler(WebSocketSessionContainer webSocketSessionContainer,
                                      FxSubscriptionSessionMap subscriptionSessionMap){
        this.webSocketSessionContainer = webSocketSessionContainer;
        this.subscriptionSessionMap = subscriptionSessionMap;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        webSocketSessionContainer.put(session.getId(), session);

        webSocketSessionContainer.forEach(entry -> logger.info(entry.getKey()));

        logger.info("Socket is open? " + session.isOpen() + " Session ID: " + session.getId());

        // Keep the session open by processing incoming messages
        Flux<String> stringFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> handleClientSubscriptionChange(session, message))
                .doFinally(signalType -> handleSessionClose(session, signalType));

        return stringFlux.then();
    }

    record CurrencyPairPayload(String currencyPair) {}

    private void handleClientSubscriptionChange(WebSocketSession session, String message) {
        logger.info("Received message: " + message);

        try {
            var t = gson.fromJson(message, ClientMessageType.class);
            var eventType = ClientMarketDataEventType.fromValue(t.type());

            switch (eventType) {
                case SUBSCRIBE_TO_MARKET_DATA:
                case UNSUBSCRIBE_FROM_MARKET_DATA:
                    Type type = TypeToken.getParameterized(ClientMessageEvent.class, CurrencyPairPayload.class).getType();
                    ClientMessageEvent<CurrencyPairPayload> event = gson.fromJson(message, type);
                    if (eventType == ClientMarketDataEventType.SUBSCRIBE_TO_MARKET_DATA)
                        subscriptionSessionMap.addSessionIdForCurrencyPair(event.payload().currencyPair, session.getId());
                    else
                        subscriptionSessionMap.removeSessionIdForCurrencyPair(event.payload().currencyPair, session.getId());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + eventType);
            }
        } catch (Throwable e) {
            logger.error("error", e);
        }
    }

    private void handleSessionClose(WebSocketSession session, SignalType signalType) {
        subscriptionSessionMap.removeSessionId(session.getId());
        webSocketSessionContainer.remove(session.getId());
        logger.info("WebSocket session closed: " + session.getId());
    }
}


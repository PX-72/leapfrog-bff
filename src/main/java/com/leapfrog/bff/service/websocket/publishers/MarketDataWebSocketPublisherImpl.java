package com.leapfrog.bff.service.websocket.publishers;

import com.leapfrog.bff.service.interfaces.MarketDataWebSocketPublisher;
import com.leapfrog.bff.service.websocket.WebSocketSessionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MarketDataWebSocketPublisherImpl implements MarketDataWebSocketPublisher {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataWebSocketPublisherImpl.class);

    private final WebSocketSessionContainer webSocketSessionContainer;

    @Autowired
    public MarketDataWebSocketPublisherImpl(WebSocketSessionContainer webSocketSessionContainer) {
        this.webSocketSessionContainer = webSocketSessionContainer;
    }

    @Override
    public void broadcast(String message) {
        for (Map.Entry<String, WebSocketSession> entry : webSocketSessionContainer.entrySet()){
            WebSocketSession session = entry.getValue();
            WebSocketMessage webSocketMessage = session.textMessage(message);
            Mono<Void> sendMono = session.send(Mono.just(webSocketMessage));
            logger.info("Sending message: " + message);
            sendMono.subscribe();
        }
    }
}

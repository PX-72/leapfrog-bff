package com.leapfrog.bff.handlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MarketDataWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        sessions.put(session.getId(), session);

        sessions.forEach((id, s) -> logger.info(id));

        logger.info("is open " + session.isOpen() + " Session ID: " + session.getId());

        Mono<Void> output = session.send(Flux.just(session.textMessage("Hello from server")));

        // Keep the session open by processing incoming messages
        Flux<String> stringFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    logger.info("Received message: " + message);
                })
                .doFinally(signalType -> {
                    sessions.remove(session.getId());
                    logger.info("WebSocket session closed: " + session.getId());
                });

        // Combine output and stringFlux into a single sequence that completes when either completes
        return output.and(stringFlux.then());
    }
}
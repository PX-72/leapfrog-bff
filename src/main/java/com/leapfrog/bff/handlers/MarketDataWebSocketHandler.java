package com.leapfrog.bff.handlers;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class MarketDataWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .send(session.receive().map(msg -> session.textMessage("ECHO: " + msg.getPayloadAsText())));
    }
}
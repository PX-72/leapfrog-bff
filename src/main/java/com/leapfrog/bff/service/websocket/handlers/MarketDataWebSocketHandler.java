package com.leapfrog.bff.service.websocket.handlers;
import com.leapfrog.bff.service.interfaces.MessageProcessor;
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

@Service
public class MarketDataWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataWebSocketHandler.class);

    private final WebSocketSessionContainer webSocketSessionContainer;

    @Autowired
    public MarketDataWebSocketHandler(WebSocketSessionContainer webSocketSessionContainer){
        this.webSocketSessionContainer = webSocketSessionContainer;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        webSocketSessionContainer.put(session.getId(), session);

        webSocketSessionContainer.forEach(entry -> logger.info(entry.getKey()));

        logger.info("Socket is open? " + session.isOpen() + " Session ID: " + session.getId());

        Mono<Void> output = session.send(Flux.just(session.textMessage("Hello from server")));

        // Keep the session open by processing incoming messages
        Flux<String> stringFlux = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    logger.info("Received message: " + message);
                })
                .doFinally(signalType -> {
                    webSocketSessionContainer.remove(session.getId());
                    logger.info("WebSocket session closed: " + session.getId());
                });

        // Combine output and stringFlux into a single sequence that completes when either completes
        return output.and(stringFlux.then());
    }
}
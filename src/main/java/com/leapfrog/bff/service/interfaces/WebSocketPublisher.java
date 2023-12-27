package com.leapfrog.bff.service.interfaces;

public interface WebSocketPublisher {
    void broadcast(String message);
    void publish(String message, String sessionId);
}

package com.leapfrog.bff.service.websocket;

import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionContainer implements Iterable<Map.Entry<String, WebSocketSession>> {
    private final Map<String, WebSocketSession> sessions;

    public WebSocketSessionContainer() {
        this.sessions = new ConcurrentHashMap<>();
    }

    public WebSocketSession put(String key, WebSocketSession session) {
        return sessions.put(key, session);
    }

    public WebSocketSession get(String key) {
        return sessions.get(key);
    }

    public WebSocketSession remove(String sessionId){
        return sessions.remove(sessionId);
    }

    public Integer size() {
        return sessions.size();
    }

    public Set<Map.Entry<String, WebSocketSession>> entrySet() {
        return sessions.entrySet();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Map.Entry<String, WebSocketSession>> iterator() {
        return sessions.entrySet().iterator();
    }
}
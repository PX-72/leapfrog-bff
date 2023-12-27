package com.leapfrog.bff.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FxSubscriptionSessionMap {
    private final Map<String, Set<String>> map = new ConcurrentHashMap<>();

    public void addSessionIdForCurrencyPair(String currencyPair, String sessionId) {
        if (!map.containsKey(currencyPair))
            map.put(currencyPair, ConcurrentHashMap.newKeySet());

        map.get(currencyPair).add(sessionId);
    }

    public List<String> getSessionIdsByCurrencyPair(String currencyPair) {
        var set = map.get(currencyPair);
        if (set == null || set.isEmpty()) return new ArrayList<>();

        return new ArrayList<>(set);
    }

    public void removeSessionIdForCurrencyPair(String currencyPair, String sessionId) {
        if (map.containsKey(currencyPair))
            map.get(currencyPair).remove(sessionId);
    }

    public void removeSessionId(String sessionId) {
        for (Set<String> sessionIdSets : map.values()) {
            sessionIdSets.remove(sessionId);
        }
    }

    public Integer size() {
        return map.size();
    }

}

package com.leapfrog.bff.service;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FxSubscriptionSessionMap {
    private final Map<String, Set<String>> map = new ConcurrentHashMap<>();

    public void put(String currencyPair, String sessionId) {
        if (!map.containsKey(currencyPair))
            map.put(currencyPair, ConcurrentHashMap.newKeySet());

        map.get(currencyPair).add(sessionId);
    }

    public List<String> get(String currencyPair) {
        var set = map.get(currencyPair);
        if (set == null || set.isEmpty()) return new ArrayList<>();

        return new ArrayList<>(set);
    }

    public void remove(String currencyPair, String sessionId) {
        if (map.containsKey(currencyPair))
            map.get(currencyPair).remove(sessionId);
    }

    public Integer size() {
        return map.size();
    }

}

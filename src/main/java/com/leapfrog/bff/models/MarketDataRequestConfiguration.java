package com.leapfrog.bff.models;

import java.math.BigDecimal;
import java.util.List;

public record MarketDataRequestConfiguration (
        String configurationId,
        Integer size,
        Integer intervalInMillis,
        Integer intervalDelayInMillis,
        List<String> ccyFilter
){}

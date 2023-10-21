package com.leapfrog.bff.service.interfaces;

import com.leapfrog.bff.models.MarketDataRequestConfiguration;
import reactor.core.publisher.Mono;

public interface MarketDataRequestService {
    Mono<Void> RequestMarketData(MarketDataRequestConfiguration requestConfiguration);
}

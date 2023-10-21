package com.leapfrog.bff.service;

import com.leapfrog.bff.models.MarketDataRequestConfiguration;
import com.leapfrog.bff.service.interfaces.MarketDataRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MarketDataRequestServiceImpl implements MarketDataRequestService {

    private final WebClient webClient;

    @Value("${market-data.server}")
    private String marketDataServer;

    @Autowired
    public MarketDataRequestServiceImpl(WebClient.Builder webClientBuilder) {
        String urlRoot = String.format("%s/api/v1/market-data/", marketDataServer);
        this.webClient = webClientBuilder.baseUrl(urlRoot).build();
    }

    @Override
    public Mono<Void> RequestMarketData(MarketDataRequestConfiguration requestConfiguration) {
        return webClient.put()
                .uri("data-request")
                .bodyValue(requestConfiguration)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

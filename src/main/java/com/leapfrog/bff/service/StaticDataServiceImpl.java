package com.leapfrog.bff.service;

import com.leapfrog.bff.service.interfaces.StaticDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StaticDataServiceImpl implements StaticDataService {

    private final WebClient webClient;

    @Autowired
    public StaticDataServiceImpl(WebClient.Builder webClientBuilder,
                                 @Value("${market-data.server}") String marketDataServer) {
        String urlRoot = String.format("%s/api/v1/static-data/", marketDataServer);
        this.webClient = webClientBuilder.baseUrl(urlRoot).build();
    }

    @Override
    public Mono<List<String>> GetCurrencyPairs() {
        return webClient.get()
                .uri("currency-pairs")
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {});
    }
}

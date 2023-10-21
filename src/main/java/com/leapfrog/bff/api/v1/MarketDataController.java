package com.leapfrog.bff.api.v1;

import com.leapfrog.bff.models.MarketDataRequestConfiguration;
import com.leapfrog.bff.service.interfaces.MarketDataRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/market-data")
public class MarketDataController {
    private static final Logger logger = LoggerFactory.getLogger(MarketDataController.class);

    private final MarketDataRequestService marketDataRequestService;

    @Autowired
    public MarketDataController(MarketDataRequestService marketDataRequestService) {
        this.marketDataRequestService = marketDataRequestService;
    }

    @PutMapping("/data-request")
    Mono<Void> replaceEmployee(@RequestBody MarketDataRequestConfiguration requestConfiguration) {
        logger.info("Market data request has been received: {}", requestConfiguration);
        return marketDataRequestService.RequestMarketData(requestConfiguration);
    }

}

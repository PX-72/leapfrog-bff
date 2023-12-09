package com.leapfrog.bff.api.v1;

import com.leapfrog.bff.service.interfaces.StaticDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/v1/static-data")
public class StaticDataController {
    private static final Logger logger = LoggerFactory.getLogger(StaticDataController.class);

    private final StaticDataService staticDataService;

    @Autowired
    public StaticDataController(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @GetMapping("/currency-pairs")
    Mono<List<String>> getCurrencyPairs() {
        logger.info("Static data currency pair request was received.");
        return staticDataService.GetCurrencyPairs();
    }

}





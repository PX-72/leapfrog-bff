package com.leapfrog.bff.models;

import java.math.BigDecimal;

public record FxMarketData(String ccyPair, BigDecimal bid, BigDecimal offer, BigDecimal low, BigDecimal high, String ecn, Integer scale) {
}

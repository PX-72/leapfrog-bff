package com.leapfrog.bff.models;

public enum ClientMarketDataEventType {
    SUBSCRIBE_TO_MARKET_DATA(0),
    UNSUBSCRIBE_FROM_MARKET_DATA(1),
    MARKET_DATA_RESPONSE(2);

    private int value;

    ClientMarketDataEventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ClientMarketDataEventType fromValue(int value) {
        for (ClientMarketDataEventType type : ClientMarketDataEventType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

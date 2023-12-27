package com.leapfrog.bff.models;

public record ClientMessageEvent<T>(int type, T payload) {}

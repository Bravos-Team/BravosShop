package com.bravos2k5.bravosshop.service;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class RedisCacheEntry<T> {

    @NonNull
    String key;

    @NonNull
    Supplier<T> fallBackFunction;

    long keyTimeout;

    TimeUnit keyTimeUnit;

    @Builder.Default
    long lockTimeout = 100;

    @Builder.Default
    TimeUnit lockTimeUnit = TimeUnit.MILLISECONDS;

    @Builder.Default
    int retryTime = 3;

    @Builder.Default
    int retryWait = 50;

}

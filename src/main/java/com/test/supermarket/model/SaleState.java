package com.test.supermarket.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

/**
 * Allowed sale states. Values are intentionally in English, so requests using
 * Spanish state names (for example "REGISTRADA") are rejected.
 */
public enum SaleState {
    REGISTERED,
    PENDING,
    PAID,
    CANCELLED;

    @JsonCreator
    public static SaleState fromValue(String value) {
        if (value == null) return null;

        return SaleState.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}

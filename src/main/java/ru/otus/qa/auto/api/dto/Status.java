package ru.otus.qa.auto.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("sold")
    SOLD
}

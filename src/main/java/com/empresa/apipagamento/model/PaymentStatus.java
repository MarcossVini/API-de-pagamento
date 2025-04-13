package com.empresa.apipagamento.model;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED;
}
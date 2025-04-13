package com.empresa.apipagamento.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    
    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(CREDIT_CARD|DEBIT_CARD|PIX|BOLETO)$", 
            message = "Payment method must be one of: CREDIT_CARD, DEBIT_CARD, PIX, BOLETO")
    private String paymentMethod;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits and 2 decimal places")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;

    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
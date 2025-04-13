package com.empresa.apipagamento.dto;

import com.empresa.apipagamento.model.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Representation of a payment response")
public class PaymentResponse {
    
    @Schema(description = "Payment unique identifier", example = "1")
    private Long id;
    
    @Schema(description = "Payment method used", example = "CREDIT_CARD")
    private String paymentMethod;
    
    @Schema(description = "Payment amount", example = "100.50")
    private BigDecimal amount;
    
    @Schema(description = "Currency code (ISO 4217)", example = "BRL")
    private String currency;
    
    @Schema(description = "Current payment status", example = "COMPLETED")
    private PaymentStatus status;
    
    @Schema(description = "Unique transaction identifier", example = "8f9d5a6b-ed71-4abc-9cf7-6348d50487ab")
    private String transactionId;
    
    @Schema(description = "Customer identifier", example = "CUST123")
    private String customerId;
    
    @Schema(description = "Payment description", example = "Payment for order #123")
    private String description;
    
    @Schema(description = "Version number for optimistic locking")
    private Long version;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    
    @Schema(description = "User who created the payment")
    private String createdBy;
    
    @Schema(description = "User who last modified the payment")
    private String updatedBy;
}
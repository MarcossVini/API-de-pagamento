package com.empresa.apipagamento.controller;

import com.empresa.apipagamento.dto.PaymentRequest;
import com.empresa.apipagamento.dto.PaymentResponse;
import com.empresa.apipagamento.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "Payment management APIs")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create a new payment", description = "Creates a new payment transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Creating new payment with method: {}", paymentRequest.getPaymentMethod());
        PaymentResponse payment = paymentService.createPayment(paymentRequest);
        log.info("Payment created successfully with ID: {}", payment.getId());
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a payment by ID", description = "Retrieves a payment by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @Parameter(description = "Payment ID", required = true)
            @PathVariable Long id) {
        log.info("Fetching payment with ID: {}", id);
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "List all payments", description = "Returns a paginated list of payments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getAllPayments(
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching payments page: {}", pageable.getPageNumber());
        Page<PaymentResponse> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Get payment by transaction ID", description = "Retrieves a payment by its transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable String transactionId) {
        log.info("Fetching payment with transaction ID: {}", transactionId);
        PaymentResponse payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Update payment status", description = "Updates the status of an existing payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @Parameter(description = "Payment ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New payment status", required = true)
            @RequestParam String status) {
        log.info("Updating payment status. ID: {}, New Status: {}", id, status);
        PaymentResponse payment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(payment);
    }
}
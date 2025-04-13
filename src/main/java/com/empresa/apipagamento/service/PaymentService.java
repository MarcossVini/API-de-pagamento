package com.empresa.apipagamento.service;

import com.empresa.apipagamento.dto.PaymentRequest;
import com.empresa.apipagamento.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentById(Long id);
    Page<PaymentResponse> getAllPayments(Pageable pageable);
    PaymentResponse getPaymentByTransactionId(String transactionId);
    PaymentResponse updatePaymentStatus(Long id, String status);
}
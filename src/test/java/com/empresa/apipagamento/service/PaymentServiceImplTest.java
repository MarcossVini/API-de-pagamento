package com.empresa.apipagamento.service;

import com.empresa.apipagamento.dto.PaymentRequest;
import com.empresa.apipagamento.dto.PaymentResponse;
import com.empresa.apipagamento.model.Payment;
import com.empresa.apipagamento.model.PaymentStatus;
import com.empresa.apipagamento.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest paymentRequest;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod("CREDIT_CARD");
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setCurrency("BRL");
        paymentRequest.setCustomerId("CUST123");
        paymentRequest.setDescription("Test payment");

        payment = new Payment();
        payment.setId(1L);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCustomerId(paymentRequest.getCustomerId());
        payment.setDescription(paymentRequest.getDescription());
        payment.setTransactionId("test-transaction-id");
    }

    @Test
    void createPayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.createPayment(paymentRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(payment.getId());
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(response.getAmount()).isEqualTo(paymentRequest.getAmount());
    }

    @Test
    void getPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPaymentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(payment.getId());
    }

    @Test
    void getPaymentById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            paymentService.getPaymentById(1L)
        );
    }

    @Test
    void getAllPayments_Success() {
        Page<Payment> paymentPage = new PageImpl<>(List.of(payment));
        when(paymentRepository.findAll(any(PageRequest.class))).thenReturn(paymentPage);

        Page<PaymentResponse> response = paymentService.getAllPayments(PageRequest.of(0, 10));

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getId()).isEqualTo(payment.getId());
    }

    @Test
    void updatePaymentStatus_Success() {
        Payment updatedPayment = new Payment();
        updatedPayment.setId(payment.getId());
        updatedPayment.setStatus(PaymentStatus.COMPLETED);
        
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        PaymentResponse response = paymentService.updatePaymentStatus(1L, "COMPLETED");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void updatePaymentStatus_InvalidStatus() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(IllegalArgumentException.class, () ->
            paymentService.updatePaymentStatus(1L, "INVALID_STATUS")
        );
    }
}
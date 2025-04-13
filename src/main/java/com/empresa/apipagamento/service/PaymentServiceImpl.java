package com.empresa.apipagamento.service;

import com.empresa.apipagamento.dto.PaymentRequest;
import com.empresa.apipagamento.dto.PaymentResponse;
import com.empresa.apipagamento.model.Payment;
import com.empresa.apipagamento.model.PaymentStatus;
import com.empresa.apipagamento.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    @Retryable(maxAttempts = 3, value = OptimisticLockingFailureException.class)
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        log.debug("Creating new payment: {}", paymentRequest);
        
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentRequest, payment);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created successfully with ID: {}", savedPayment.getId());
        
        return convertToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "payments", key = "#id")
    public PaymentResponse getPaymentById(Long id) {
        log.debug("Fetching payment with ID: {}", id);
        
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
            
        return convertToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        log.debug("Fetching payments page: {}", pageable.getPageNumber());
        
        return paymentRepository.findAll(pageable)
            .map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "payments", key = "#transactionId")
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        log.debug("Fetching payment with transaction ID: {}", transactionId);
        
        Payment payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with transaction id: " + transactionId));
            
        return convertToResponse(payment);
    }

    @Override
    @Transactional
    @Retryable(maxAttempts = 3, value = OptimisticLockingFailureException.class)
    public PaymentResponse updatePaymentStatus(Long id, String status) {
        log.debug("Updating payment status. ID: {}, New Status: {}", id, status);
        
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
            
        try {
            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
            payment.setStatus(newStatus);
            
            Payment updatedPayment = paymentRepository.save(payment);
            log.info("Payment status updated successfully. ID: {}, New Status: {}", id, newStatus);
            
            return convertToResponse(updatedPayment);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment status: {}", status);
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
    }

    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        BeanUtils.copyProperties(payment, response);
        return response;
    }
}
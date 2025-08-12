package com.empresa.apipagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ApiPagamentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPagamentoApplication.class, args);
    }
}
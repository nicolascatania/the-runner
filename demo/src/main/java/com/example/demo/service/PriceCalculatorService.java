package com.example.demo.service;

import com.example.demo.domain.ProductPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;


@Service
@Slf4j
@RequiredArgsConstructor
public class PriceCalculatorService {

    // Injected from configuration class
    private final ExecutorService executor;

    public ProductPrice calculate(String productId) {
        try {
            var futureBase = executor.submit(() -> getBasePrice(productId));
            var futureTax = executor.submit(this::getExternalTax);
            var futureDiscount = executor.submit(this::getUserDiscount);

            double base = futureBase.get();
            double tax = futureTax.get();
            double discount = futureDiscount.get();

            double finalPrice = (base * (1 + tax)) - discount;

            return new ProductPrice(productId, finalPrice, Thread.currentThread().toString());

        } catch (Exception e) {
            log.error("Error calculating price for product: {}", productId, e);
            throw new RuntimeException("Calculation failed", e);
        }
    }

    // I/O Simulations (Base de Datos, APIs, etc.)
    // Made protected to allow tests to override and avoid Thread.sleep() during unit tests
    @SuppressWarnings("unused")
    protected double getBasePrice(String id) throws InterruptedException {
        Thread.sleep(200);
        return 100.0;
    }

    protected double getExternalTax() throws InterruptedException {
        Thread.sleep(500);
        return 0.21;
    }

    protected double getUserDiscount() throws InterruptedException {
        Thread.sleep(300);
        return 10.0;
    }
}
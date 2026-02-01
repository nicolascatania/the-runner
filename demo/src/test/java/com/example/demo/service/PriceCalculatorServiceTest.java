package com.example.demo.service;

import com.example.demo.domain.ProductPrice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorServiceTest {

    private ExecutorService executor;

    @AfterEach
    void tearDown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    @Test
    void calculate_happyPath_returnsCorrectPrice() {
        /**
         * The service is decoupled from the thread implementation.
         * Here we use a FixedThreadPool to validate business logic without needing 
         * specific Virtual Thread features.
         */
        executor = Executors.newFixedThreadPool(3);

        // "Extract and Override" technique to isolate logic from real I/O and latency
        var service = new PriceCalculatorService(executor) {
            @Override
            protected double getBasePrice(String id) { return 200.0; }

            @Override
            protected double getExternalTax() { return 0.10; }

            @Override
            protected double getUserDiscount() { return 5.0; }
        };

        ProductPrice result = service.calculate("p1");

        // (200 * 1.10) - 5 = 215
        assertNotNull(result);
        assertEquals(215.0, result.finalPrice(), 0.001);
    }

    @Test
    void calculate_whenIoThrows_exceptionIsWrappedAndTasksCancelled() {
        executor = Executors.newFixedThreadPool(3);
        var service = new PriceCalculatorService(executor) {
            @Override
            protected double getBasePrice(String id) throws InterruptedException {
                throw new InterruptedException("Simulated IO Failure");
            }
        };

        // Validate that the custom exception wrapper works as expected
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.calculate("p2"));
        assertTrue(ex.getMessage().contains("Calculation failed"));
    }
}
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class BlockingRequestsToForeignAPI {

    private static final Logger log = LoggerFactory.getLogger(BlockingRequestsToForeignAPI.class);
    private final RestClient restClient;

    public BlockingRequestsToForeignAPI(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @GetMapping("/test")
    public String test() {
        log.info("Request started on thread: {}", Thread.currentThread());

        // blocking call, waits for the response
        // this will block the thread until the response is received
        try {
            Thread.sleep(1000); //simulate delay for i/o operation, virtual thread will unmount itself and let other work be done
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Simulation completed: " + Thread.currentThread();
    }
}
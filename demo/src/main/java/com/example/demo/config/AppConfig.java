package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        // We configure the HttpClient to use virtual threads, avoiding internal perfomance issues
        var httpClient = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .build();

        return RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean
    public ExecutorService executorService() {
        // This allows us to use virtual threads in our application, mock the executor for testing, avoids memory leaks (beans are controlled by spring)
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
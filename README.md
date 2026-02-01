# 🚀 The Runner: Spring Boot Virtual Threads Orchestration

A high-performance demonstration project built with **Spring Boot 4.0.2** and **Java 21**, focused on leveraging **Virtual Threads (Project Loom)** for efficient microservice orchestration and resilient concurrent processing.

It is super simple, simulating I/O operations like db searches and external services comms. The objective is to learn, no to do anything sophisticated.


## 📌 Project Overview

This application simulates a real-world "Price Engine" that must gather data from multiple sources (Database, External Tax API, and User Discount Service) to calculate a final product price. The core objective is to demonstrate how **Virtual Threads** allow for massive concurrency without the overhead of traditional Platform Threads.

### Key Highlights:
* **Non-blocking I/O Simulation**: Uses `Thread.sleep` to mimic real-world network latency, proving that Virtual Threads release the underlying carrier thread during wait times.
* **Manual Structured Concurrency**: Implements a "fail-fast" mechanism where if one sub-task fails, all other pending tasks are cancelled immediately to conserve system resources.
* **Advanced Testing Patterns**: Utilizes the "Extract and Override" pattern to unit test asynchronous logic without slowing down the CI/CD pipeline.

---

## 🛠 Tech Stack

* **Java 21**: Utilizing the latest LTS features.
* **Spring Boot 4.0.2**: Pre-configured for high-scale web traffic.
* **Project Loom**: Implementation of Virtual Threads.
* **Lombok & Slf4j**: For clean, observable code.
* **k6**: For load testing and performance benchmarking.

---

## 🏗 Architectural Design

### 1. Decoupled Thread Management
The application doesn't manually manage thread lifecycles. Instead, it relies on a central `ExecutorService` bean. In production, this is backed by `Executors.newVirtualThreadPerTaskExecutor()`, while in testing, it can be swapped for a `FixedThreadPool`.



### 2. Resilience and Cancellation
The orchestration logic is designed to be "interruption-aware." By using `future.cancel(true)` in the exception catch blocks, the system ensures that no "zombie tasks" continue running if the main request has already failed.

### 3. Latency Optimization
By executing sub-tasks in parallel, the total request duration is reduced from a sequential **1000ms** to a concurrent **~500ms** (the duration of the longest task).

---

## 📊 Performance Metrics (k6 Results)

Stress testing with **1,000 Concurrent Virtual Users (VUs)** yielded the following results on a standard 12-core machine:

| Metric | Result |
| :--- | :--- |
| **Throughput** | ~2,000 requests per second |
| **P95 Latency** | 524.38ms |
| **Error Rate** | < 3% (OS Socket Limit reached) |
| **Worker Threads** | Only 12 Carrier Threads utilized |

---

## 🚀 Getting Started

### Prerequisites
* JDK 21 or higher.
* Maven.

### Configuration
Ensure Virtual Threads are enabled in your `src/main/resources/application.yaml`: 
spring:
  threads:
    virtual:
      enabled: true

### Running
./mvnw spring-boot:run

### Testing the Orchestrator
curl http://localhost:8080/api/products/1/price


### 🧪 Unit Testing
The test suite validates both the mathematical correctness and the error-handling wrappers:
./mvnw test


### Stress Testing (k6)
Test how the app responds to a stress test
In the project root directory

```k6 run product_price.js```

package com.example.demo.controller;

import com.example.demo.domain.ProductPrice;
import com.example.demo.service.PriceCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final PriceCalculatorService priceService;

    @GetMapping("/{id}/price")
    public ProductPrice getPrice(@PathVariable String id) {
        return priceService.calculate(id);
    }
}
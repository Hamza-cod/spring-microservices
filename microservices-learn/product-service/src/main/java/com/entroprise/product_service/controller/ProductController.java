package com.entroprise.product_service.controller;

import com.entroprise.product_service.dto.request.ProductCreateRequest;
import com.entroprise.product_service.dto.response.ProductResponse;
import com.entroprise.product_service.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private  final ProductService productService;
    @PostMapping
    @ResponseStatus(CREATED)
    public void createProduct(@RequestBody ProductCreateRequest createRequest){
       productService.createProduct(createRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}

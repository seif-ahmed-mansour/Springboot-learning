package com.demo.crud.products.controller;

import com.demo.crud.products.model.Product;
import com.demo.crud.products.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<com.demo.crud.products.model.Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public com.demo.crud.products.model.Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }
}

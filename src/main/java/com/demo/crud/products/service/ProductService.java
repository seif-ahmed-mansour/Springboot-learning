package com.demo.crud.products.service;

import com.demo.crud.config.httpClient.HttpClientFactory;
import com.demo.crud.config.httpClient.HttpClientWrapper;
import com.demo.crud.products.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final HttpClientWrapper client;

    public ProductService() {
        this.client = HttpClientFactory.create(HttpClientFactory.ClientType.OKHTTP);
    }

    public List<Product> getAllProducts() {
        Product[] products = client.sendRequest(
                "https://fakestoreapi.com/products",
                HttpMethod.GET,
                null,
                null,
                null,
                Product[].class
        );
        return Arrays.asList(products);
    }

    public Product getProductById(int id) {
        return client.sendRequest(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.GET,
                null,
                null,
                null,
                Product.class
        );
    }

    public Product createProduct(Product product) {
        return client.sendRequest(
                "https://fakestoreapi.com/products",
                HttpMethod.POST,
                null,
                null,
                product,
                Product.class
        );
    }

    public Product updateProduct(int id, Product product) {
        return client.sendRequest(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT,
                null,
                null,
                product,
                Product.class
        );
    }

    public String deleteProduct(int id) {
        return client.sendRequest(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.DELETE,
                null,
                null,
                null,
                String.class
        );
    }
}

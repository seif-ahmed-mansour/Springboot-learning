package com.demo.crud.products.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private String image;
    private String category;

}

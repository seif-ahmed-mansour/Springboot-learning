package com.demo.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CrudApplication {


	public static void main(String[] args) {

		SpringApplication.run(CrudApplication.class, args);
        System.out.println("===== Server Started =====");
	}


}

package com.demo.crud.utils;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StandardResponse {
    private String code;
    private String status;
    private String message;
    private HttpStatus httpStatus;
}

package com.demo.crud.users.dto.response;

import com.demo.crud.utils.StandardResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO extends StandardResponse {
    private Long id;
    private String name;
    private String email;
}

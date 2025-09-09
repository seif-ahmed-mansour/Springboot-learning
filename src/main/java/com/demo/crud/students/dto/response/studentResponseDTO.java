package com.demo.crud.students.dto.response;

import com.demo.crud.utils.StandardResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class studentResponseDTO extends StandardResponse {
    private StudentDataDTO data;
}

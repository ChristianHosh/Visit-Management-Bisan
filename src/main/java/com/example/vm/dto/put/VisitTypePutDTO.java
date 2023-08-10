package com.example.vm.dto.put;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Builder
public class VisitTypePutDTO {

        @Size(min = 3, max = 30, message = "Invalid name: Must be of 3 - 30 characters")
        private String name;


}

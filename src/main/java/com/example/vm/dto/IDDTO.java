package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class IDDTO {

        @NotNull(message = "Invalid ID :ID is NULL")
        private int id;

}

package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UUIDDTO {

    @NotNull(message = "Invalid UUID : uuid is NULL")
    private UUID uuid;
}

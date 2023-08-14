package com.example.vm.dto.put;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VisitTypePutDTO {

        @JsonCreator
        public VisitTypePutDTO(@JsonProperty("name") String name) {
                this.name = name;
        }

        @Size(min = 3, max = 30, message = "Invalid name: Must be of 3 - 30 characters")
        private String name;


}

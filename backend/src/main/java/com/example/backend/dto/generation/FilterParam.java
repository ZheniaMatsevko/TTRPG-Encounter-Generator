package com.example.backend.dto.generation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterParam {
    @NotBlank
    private String propertyName;

    @NotBlank
    private String value;

    @NotBlank
    @NotNull
    private Quantity quantity;
}

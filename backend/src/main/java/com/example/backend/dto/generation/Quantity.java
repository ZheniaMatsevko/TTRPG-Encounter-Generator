package com.example.backend.dto.generation;

import com.example.backend.dto.enums.QuantityRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quantity {
    private QuantityRequirement quantityRequirement;
    private int number;
}

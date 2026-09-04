package com.example.marketflow.products;

import jakarta.validation.constraints.NotNull;

public record UpdateProductAvailabilityRequest(
        @NotNull(message = "Active value is required") Boolean active
) {
}

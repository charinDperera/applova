package com.applova.charin.assessment.dto;

import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRequestDTO {
    @Valid
    @NotNull(message = "List of products is required")
    @NotEmpty(message = "List of products cannot be empty")
    private List<Product> products;

    @NotNull(message = "Waiter name required")
    @NotEmpty(message = "Waiter name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z .]*$",message = "Cannot contain numbers")
    private String waiter;

    //To ensure that the user cannot input subTotal and Total
    @Max(value = 0, message = "Subtotal will be calculated, not required")
    private double subTotal;

    @Max(value = 0, message = "Total will be calculated, not required")
    private double total;

    @NotNull(message = "Taxes cannot be null")
    @Min(value=0, message = "Taxes cannot be negative")
    @Max(value = 100, message = "Taxes cannot be above 100")
    private int taxes;

    @NotNull(message = "Service charge cannot be null")
    @Min(value=0, message = "Service charge cannot be negative")
    @Max(value = 100, message = "Service charge cannot be above 100")
    private int serviceCharge;

    @NotNull(message = "Discount cannot be null")
    @Min(value=0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot be above 100")
    private int discount;
}

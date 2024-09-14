package com.applova.charin.assessment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product name is required")
    @NotEmpty(message = "Product name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z .]*$",message = "Cannot contain numbers")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be less than 1")
    private int quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be less than 1")
    private double price;
}

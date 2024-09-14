package com.applova.charin.assessment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "Order Num is created automatically")
    private Long orderNum;

    @OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL)
    @JoinColumn(name="order_num", referencedColumnName = "orderNum")
    private List<Product> products;

    @NotNull(message = "Waiter name required")
    @NotEmpty(message = "Waiter name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z .]*$",message = "Cannot contain numbers")
    private String waiter;

    @Min(value = 0, message = "SubTotal has become negative")
    private double subTotal;

    @Min(value = 0, message = "Total has become negative")
    private double total;

    @Transient
    @NotNull(message = "Taxes cannot be null")
    @Min(value=0, message = "Taxes cannot be negative")
    @Max(value = 100, message = "Taxes cannot be above 100")
    private int taxes;

    @Transient
    @NotNull(message = "Service charge cannot be null")
    @Min(value=0, message = "Service charge cannot be negative")
    @Max(value = 100, message = "Service charge cannot be above 100")
    private int serviceCharge;

    @Transient
    @NotNull(message = "Discount cannot be null")
    @Min(value=0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot be above 100")
    private int discount;
}

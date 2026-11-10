package com.test.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Venta
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    // Producto
    @ManyToOne(fetch = FetchType.LAZY)
    private Product prod;

    private Integer quantProd;
    private Double price;
}

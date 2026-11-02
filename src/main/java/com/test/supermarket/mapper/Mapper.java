package com.test.supermarket.mapper;

import com.test.supermarket.dto.BranchDTO;
import com.test.supermarket.dto.ProductDTO;
import com.test.supermarket.dto.SaleDTO;
import com.test.supermarket.dto.SaleDetailDTO;
import com.test.supermarket.model.Branch;
import com.test.supermarket.model.Product;
import com.test.supermarket.model.Sale;

public class Mapper {
    // Mapper Product to ProductDTO
    public static ProductDTO toDTO(Product p) {
        if (p == null) return null;

        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .category(p.getCategory())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .build();
    }

    // Mapper Branch to BranchDTO
    public static BranchDTO toDTO(Branch b) {
        if (b == null) return null;

        return BranchDTO.builder()
                .id(b.getId())
                .name(b.getName())
                .address(b.getAddress())
                 .build();
    }

    // Mapper Sale to SaleDTO
    public static SaleDTO toDTO(Sale s) {
        if (s == null) return null;

        var details = s.getDetails().stream().map(det ->
                SaleDetailDTO.builder()
                        .id(det.getId())
                        .nameProd(det.getProd().getName())
                        .quantProd(det.getQuantProd())
                        .price(det.getPrice())
                        .subtotal(det.getPrice() * det.getQuantProd())
                        .build()
                ).toList();

        var total = details.stream()
                .map(SaleDetailDTO::getSubtotal)
                .reduce(0.0, Double::sum);

        return SaleDTO.builder()
                .id(s.getId())
                .date(s.getDate())
                .state(s.getState())
                .branchId(s.getBranch().getId())
                .details(details)
                .total(total)
                .build();
    }
}

package maper;

import dto.BranchDTO;
import dto.ProductDTO;
import dto.SaleDTO;
import dto.SaleDetailDTO;
import model.Branch;
import model.Product;
import model.Sale;

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
                .build();
    }
}

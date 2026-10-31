package dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SaleDetailDTO {
    private Long id;
    private String nameProd;
    private Integer quantProd;
    private Double price;
    private Double subtotal;
}

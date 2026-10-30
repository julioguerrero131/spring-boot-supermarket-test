package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SaleDetailDTO {
    private Long id;
    private String nameProd;
    private Integer quantProd;
    private Double price;
    private Double subtotal;
}

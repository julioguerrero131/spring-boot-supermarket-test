package dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SaleDTO {
    // Datos Venta
    private Long id;
    private LocalDate date;
    private String state;

    // Datos Sucursal
    private Long branchId;

    // Lista Detalles
    private List<SaleDetailDTO> details;

    // Total
    private Double total;
}

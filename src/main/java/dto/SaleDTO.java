package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SaleDTO {
    // Datos Venta
    private Long id;
    private LocalDate date;
    private String state;

    // Datos Sucursal
    private Long branchId;

    // Lista Detalles
    private List<SaleDetailDTO> detail;

    // Total
    private Double total;
}

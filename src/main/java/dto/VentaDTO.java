package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VentaDTO {
    // Datos Venta
    private Long id;
    private LocalDate fecha;
    private String estado;

    // Datos Sucursal
    private Long idSucursal;

    // Lista Detalles
    private List<DetalleVentaDTO> detalle;

    // Total
    private Double total;
}

package controller;

import dto.SaleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ISaleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private ISaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getSales());
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO) {
        SaleDTO s = saleService.createSale(saleDTO);
        return ResponseEntity.created(URI.create("/api/sales/" + s.getId())).body(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(
            @PathVariable Long id,
            @RequestBody SaleDTO saleDTO
    ) {
        return ResponseEntity.ok(saleService.updateSale(id, saleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}

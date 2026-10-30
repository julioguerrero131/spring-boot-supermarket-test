package service;

import dto.SaleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService implements ISaleService {
    @Override
    public List<SaleDTO> getSales() {
        return List.of();
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDto) {
        return null;
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDto) {
        return null;
    }

    @Override
    public void deleteSale(Long id) {

    }
}

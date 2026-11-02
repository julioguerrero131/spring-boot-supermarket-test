package com.test.supermarket.service;

import com.test.supermarket.dto.SaleDTO;

import java.util.List;

public interface ISaleService {

    List<SaleDTO> getSales();
    SaleDTO createSale(SaleDTO saleDto);
    SaleDTO updateSale(Long id, SaleDTO saleDto);
    void deleteSale(Long id);

}

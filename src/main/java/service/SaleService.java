package service;

import dto.SaleDTO;
import dto.SaleDetailDTO;
import exception.NotFoundException;
import maper.Mapper;
import model.Branch;
import model.Product;
import model.Sale;
import model.SaleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BranchRepository;
import repository.ProductRepository;
import repository.SaleRepository;

import java.util.ArrayList;
import java.util.List;

import static maper.Mapper.toDTO;

@Service
public class SaleService implements ISaleService {
    @Autowired
    private SaleRepository saleRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private BranchRepository branchRepo;

    @Override
    public List<SaleDTO> getSales() {
        List<Sale> sales = saleRepo.findAll();
        List<SaleDTO> salesDto = new ArrayList<>();
        SaleDTO saleDto;
        for (Sale s : sales) {
            saleDto = toDTO(s);
            salesDto.add(saleDto);
        }

        return salesDto;
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDto) {
        // Validation
        if (saleDto == null) throw new RuntimeException("saleDto is null");
        if (saleDto.getBranchId() == null) throw new RuntimeException("Branch of saleDto is null");
        if (saleDto.getDetails() == null || saleDto.getDetails().isEmpty())
            throw new RuntimeException("details of saleDto is null");

        // Branch Search
        Branch b = branchRepo.findById(saleDto.getBranchId()).orElse(null);
        if (b == null)
            throw new NotFoundException("branch not found");

        // Create Sale
        Sale sale = new Sale();
        sale.setDate(saleDto.getDate());
        sale.setState(saleDto.getState());
        sale.setBranch(b);
        sale.setTotal(saleDto.getTotal());

        // Sale Detail
        List<SaleDetail> details = new ArrayList<>();

        for (SaleDetailDTO sddto : saleDto.getDetails()) {
            Product p = productRepo.findByName(sddto.getNameProd()).orElse(null);
            if (p == null)
                throw new NotFoundException("product not found: " + sddto.getNameProd());

            SaleDetail detail = new SaleDetail();
            detail.setProd(p);
            detail.setPrice(sddto.getPrice());
            detail.setQuantProd(sddto.getQuantProd());
            detail.setSale(sale);

            details.add(detail);
        }

        // Save Details
        sale.setDetails(details);

        // Save in BD
        return Mapper.toDTO(saleRepo.save(sale));
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDto) {
        Sale s = saleRepo.findById(id).orElse(null);
        if (s == null) throw new RuntimeException("sale not found");

        if (saleDto.getDate() != null)
            s.setDate(saleDto.getDate());
        if (saleDto.getState() != null)
            s.setState(saleDto.getState());
        if (saleDto.getTotal() != null)
            s.setTotal(saleDto.getTotal());
        if (saleDto.getBranchId() != null) {
            Branch b = branchRepo.findById(saleDto.getBranchId()).orElse(null);
            s.setBranch(b);
        }

        return Mapper.toDTO(saleRepo.save(s));
    }

    @Override
    public void deleteSale(Long id) {
        Sale s = saleRepo.findById(id).orElse(null);
        if (s == null) throw new RuntimeException("sale not found");
        saleRepo.delete(s);
    }
}

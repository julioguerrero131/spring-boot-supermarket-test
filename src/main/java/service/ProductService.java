package service;

import dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Override
    public List<ProductDTO> getProduct() {
        return List.of();
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDto) {
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}

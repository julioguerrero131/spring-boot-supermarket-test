package service;

import dto.ProductDTO;

import java.util.List;

public interface IProductService {

    List<ProductDTO> getProducts();
    ProductDTO createProduct(ProductDTO productDto);
    ProductDTO updateProduct(Long id, ProductDTO productDto);
    void deleteProduct(Long id);

}

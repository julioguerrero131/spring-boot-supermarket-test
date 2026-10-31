package service;

import dto.ProductDTO;
import exception.NotFoundException;
import maper.Mapper;
import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository repo;

    @Override
    public List<ProductDTO> getProduct() {
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDto) {
        var prod = Product.builder()
                .name(productDto.getName())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();

        return Mapper.toDTO(repo.save(prod));
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        // if exists
        Product prod = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find the product."));

        prod.setName(productDto.getName());
        prod.setCategory(productDto.getCategory());
        prod.setPrice(productDto.getPrice());
        prod.setQuantity(productDto.getQuantity());

        return Mapper.toDTO(repo.save(prod));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw  new NotFoundException("Can't find the product.");
        }

        repo.deleteById(id);
    }
}

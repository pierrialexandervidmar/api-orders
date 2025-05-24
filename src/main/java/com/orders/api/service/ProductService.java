package com.orders.api.service;

import com.orders.api.entity.Product;
import com.orders.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired  // Injeção de dependência via construtor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE
    public Product create(Product createProductDto) {
        // No NestJS tem create() + save(), mas no Spring só precisa do save()
        return productRepository.save(createProductDto);
    }

    // FIND ALL
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // FIND ONE
    public Optional<Product> findOne(String id) {
        return productRepository.findById(id);
    }

    // UPDATE
    public Product update(String id, Product updateProductDto) {
        return productRepository.findById(id).map(product -> {
            // Aqui você atualiza os campos desejados manualmente
            product.setName(updateProductDto.getName());
            product.setPrice(updateProductDto.getPrice());
            // Adicione outros campos conforme a entidade
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // DELETE
    public void remove(String id) {
        productRepository.deleteById(id);
    }
}

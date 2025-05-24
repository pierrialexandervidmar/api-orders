package com.orders.api.resource;

import com.orders.api.entity.Product;
import com.orders.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product createProductDto) {
        Product createdProduct = productService.create(createProductDto);
        return ResponseEntity.ok(createdProduct);
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    // FIND ONE
    @GetMapping("/{id}")
    public ResponseEntity<Product> findOne(@PathVariable String id) {
        return productService.findOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @Valid @RequestBody Product updateProductDto) {
        try {
            Product updatedProduct = productService.update(id, updateProductDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable String id) {
        productService.remove(id);
        return ResponseEntity.noContent().build();
    }
}

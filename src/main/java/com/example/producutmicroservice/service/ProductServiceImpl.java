package com.example.producutmicroservice.service;

import com.example.producutmicroservice.dto.ProductDTO;
import com.example.producutmicroservice.exception.ResourceNotFoundException;
import com.example.producutmicroservice.mapper.ProductMapper;
import com.example.producutmicroservice.model.Product;
import com.example.producutmicroservice.model.ProductCategory;
import com.example.producutmicroservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(ProductMapper::toDTO);
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toDTO);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    public ProductDTO deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        Product product = productOptional.get();
        productRepository.delete(product);
        return ProductMapper.toDTO(product);
    }
    public ProductDTO updateProductPartially(Long id, Map<String, Object> updates) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        Product product = productOptional.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> product.setName((String) value);
                case "category" -> product.setCategory(ProductCategory.valueOf((String) value));
                case "price" -> product.setPrice((Double) value);
            }
        });

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toDTO(updatedProduct);
    }
}
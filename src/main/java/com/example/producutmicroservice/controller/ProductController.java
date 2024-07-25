package com.example.producutmicroservice.controller;

import com.example.producutmicroservice.dto.ProductDTO;
import com.example.producutmicroservice.exception.ResourceNotFoundException;
import com.example.producutmicroservice.exception.SuccessResponse;
import com.example.producutmicroservice.model.Product;
import com.example.producutmicroservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends AbstractController{

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPage = productService.getAllProducts(pageable);
        return createSuccessResponse(productPage.getContent(), "Products fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return createSuccessResponse(productDTO, "Product fetched successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return createSuccessResponse(createdProduct, "Product created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> deleteProduct(@PathVariable Long id) {
        ProductDTO deletedProduct = productService.deleteProduct(id);
        return createSuccessResponse(deletedProduct, "Product deleted successfully", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> updateProductPartially(@PathVariable Long id,
                                                                              @RequestBody Map<String, Object> updates) {
        ProductDTO updatedProduct = productService.updateProductPartially(id, updates);
        return createSuccessResponse(updatedProduct, "Product updated successfully", HttpStatus.OK);
    }
}
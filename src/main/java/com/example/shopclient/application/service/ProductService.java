package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void saveProduct(Product product, MultipartFile file);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
}

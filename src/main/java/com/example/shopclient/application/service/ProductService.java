package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Address;
import com.example.shopclient.application.model.Product;
import com.example.shopclient.security.model.Client;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product, MultipartFile file);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);

    void buyProduct(Long product_id, Address address, Client client);
    List<Product> getMyProducts(Long id);
    void updateProduct(Product product, MultipartFile file, Long id);
}

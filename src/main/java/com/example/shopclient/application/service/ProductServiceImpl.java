package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Product;
import com.example.shopclient.security.model.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final static String productPath = "http://localhost:8080/service/product";
    private final static String productPathWithId = "http://localhost:8080/service/product/{id}";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void saveProduct(Product product, MultipartFile file) {
        try{
            Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            product.setSeller(seller);
            restTemplate.postForEntity(productPath, product, Product.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath, Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public Product getProductById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        return restTemplate.getForObject(productPathWithId, Product.class, map);
    }

    @Override
    public void deleteProductById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        restTemplate.delete(productPathWithId, map);
    }
}

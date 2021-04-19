package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Address;
import com.example.shopclient.application.model.Notification;
import com.example.shopclient.application.model.Product;
import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final static String productPath = "http://localhost:8080/service/product";
    private final static String productPathWithId = "http://localhost:8080/service/product/{id}";
    private final static String clientPath = "http://localhost:8080/service/client";

    private final RestTemplate restTemplate = new RestTemplate();

    private final NotificationService notificationService;

    public ProductServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

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

    @Override
    public void buyProduct(Long product_id, Address address, Client client) {
        Notification notification = new Notification();
        Product product = getProductById(product_id);
        Seller seller = product.getSeller();
        notification.setSeller(seller);
        notification.setText(client.getName() + " " + client.getSurname() + " have ordered " + product.getDescription()
        + ". Client contacts:" + " email - " + client.getEmail() + ", phone - " + client.getPhone()
        + ". Address: country - " + address.getCountry() + ", region - " + address.getRegion() + ", city - " + address.getCity()
        + ", post office number - " + address.getPost_office_number());

        client.setOrders(Collections.singletonList(product));
        restTemplate.put(clientPath, client, Client.class);
        client.setOrders(null);
        notificationService.saveNotification(notification);
    }

    @Override
    public List<Product> getMyOrders(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPathWithId+"/myOrders", Product[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public List<Product> getMyProducts(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPathWithId+"/myProducts", Product[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public void updateProduct(Product product, MultipartFile file, Long id) {
        Product dbProduct = getProductById(id);

        if (!file.getOriginalFilename().equals("")) {
            try {
                dbProduct.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dbProduct.setTitle(product.getTitle());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        restTemplate.put(productPath, dbProduct, Product.class);
    }
}

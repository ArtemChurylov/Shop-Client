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
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final static String productPath = "https://shop-server-artem.herokuapp.com/service/product";
    private final static String productPathWithId = "https://shop-server-artem.herokuapp.com/service/product/{id}";
    private final static String clientPath = "https://shop-server-artem.herokuapp.com/service/client";

    private final RestTemplate restTemplate = new RestTemplate();

    private final NotificationService notificationService;

    public ProductServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Add seller to product and send request to save it
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

    // Send request to get all products
    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath, Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get product by id
    @Override
    public Product getProductById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        return restTemplate.getForObject(productPathWithId, Product.class, map);
    }

    // Send request to delete product by id
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
        // Set seller for notification
        notification.setSeller(seller);
        // Create text for notification
        notification.setText(client.getName() + " " + client.getSurname() + " have ordered " + product.getDescription()
        + ". Client contacts:" + " email - " + client.getEmail() + ", phone - " + client.getPhone()
        + ". Address: country - " + address.getCountry() + ", region - " + address.getRegion() + ", city - " + address.getCity()
        + ", post office number - " + address.getPost_office_number());

        // Set product to client
        client.setOrders(Collections.singletonList(product));
        // Send request to update client with new product
        restTemplate.put(clientPath, client, Client.class);
        client.setOrders(null);
        // Save notification
        notificationService.saveNotification(notification);
    }

    // Send request to get all client orders
    @Override
    public List<Product> getMyOrders(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPathWithId+"/myOrders", Product[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get all seller products
    @Override
    public List<Product> getMyProducts(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPathWithId+"/myProducts", Product[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to update product
    @Override
    public void updateProduct(Product product, MultipartFile file, Long id) {
        Product dbProduct = getProductById(id);

        // If file != null, set new one
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

    // Send request to get all footwear
    @Override
    public List<Product> getFootwear() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath+"/footwear", Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get all clothes
    @Override
    public List<Product> getClothes() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath+"/clothes", Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get all accessories
    @Override
    public List<Product> getAccessories() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath+"/accessories", Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get all cosmetics
    @Override
    public List<Product> getCosmetics() {
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productPath+"/cosmetics", Product[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Returns all products, where title contains search result
    @Override
    public List<Product> search(String result) {
        return getAllProducts().stream().filter(product -> product.getTitle().toLowerCase().contains(result.toLowerCase()))
                .collect(Collectors.toList());
    }
}

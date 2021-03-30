package com.example.shopclient.security.service;

import com.example.shopclient.security.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final static String clientPath = "http://localhost:8080/service/client";
    private final static String clientPathWithId = "http://localhost:8080/service/client/{id}";
    private final static String sellerPath = "http://localhost:8080/service/seller";
    private final static String sellerPathWithId = "http://localhost:8080/service/seller/{id}";

    private final RestTemplate restTemplate = new RestTemplate();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveClient(TempUser tempUser) {
        Client client = new Client();
        client.setEmail(tempUser.getEmail());
        client.setName(tempUser.getName());
        client.setSurname(tempUser.getSurname());
        client.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        client.setPhone(tempUser.getPhone());
        client.setRole(Role.CLIENT);
        restTemplate.postForEntity(clientPath, client, Client.class);
    }

    @Override
    public List<Client> getAllClients() {
        ResponseEntity<Client[]> responseEntity = restTemplate.getForEntity(clientPath, Client[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public Client getClientById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        Client client = restTemplate.getForObject(clientPathWithId, Client.class, map);
        return client;
    }

    @Override
    public void deleteClientById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        restTemplate.delete(clientPathWithId, map);
    }

    @Override
    public void saveSeller(TempUser tempUser) {
        Seller seller = new Seller();
        seller.setEmail(tempUser.getEmail());
        seller.setName(tempUser.getName());
        seller.setSurname(tempUser.getSurname());
        seller.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        seller.setPhone(tempUser.getPhone());
        seller.setRole(Role.SELLER);
        restTemplate.postForEntity(sellerPath, seller, Seller.class);
    }

    @Override
    public List<Seller> getAllSellers() {
        ResponseEntity<Seller[]> responseEntity = restTemplate.getForEntity(sellerPath, Seller[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public Seller getSellerById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        Seller seller = restTemplate.getForObject(sellerPathWithId, Seller.class, map);
        return seller;
    }

    @Override
    public void deleteSellerById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        restTemplate.delete(sellerPathWithId, map);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = getAllClients().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        if (client.isPresent()) return client.get();
        Optional<Seller> seller = getAllSellers().stream().filter(s -> s.getEmail().equals(email)).findFirst();
        if (seller.isPresent()) return seller.get();
        else throw new IllegalStateException("User not found");
    }
}

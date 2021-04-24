package com.example.shopclient.security.service;

import com.example.shopclient.security.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final static String clientPath = "https://shop-server-artem.herokuapp.com/service/client";
    private final static String clientPathWithId = "https://shop-server-artem.herokuapp.com/service/client/{id}";
    private final static String sellerPath = "https://shop-server-artem.herokuapp.com/service/seller";
    private final static String sellerPathWithId = "https://shop-server-artem.herokuapp.com/service/seller/{id}";

    private final RestTemplate restTemplate = new RestTemplate();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Send request to save Client
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

    // Send request to get all clients
    @Override
    public List<Client> getAllClients() {
        ResponseEntity<Client[]> responseEntity = restTemplate.getForEntity(clientPath, Client[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get client by id
    @Override
    public Client getClientById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        Client client = restTemplate.getForObject(clientPathWithId, Client.class, map);
        return client;
    }

    // Send request to delete client by id
    @Override
    public void deleteClientById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        restTemplate.delete(clientPathWithId, map);
    }

    // Send request to save seller
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

    // Send request to get all sellers
    @Override
    public List<Seller> getAllSellers() {
        ResponseEntity<Seller[]> responseEntity = restTemplate.getForEntity(sellerPath, Seller[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to get seller by id
    @Override
    public Seller getSellerById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        Seller seller = restTemplate.getForObject(sellerPathWithId, Seller.class, map);
        return seller;
    }

    // Send request to delete seller by id
    @Override
    public void deleteSellerById(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        restTemplate.delete(sellerPathWithId, map);
    }

    // Send request to update user
    @Override
    public void updateUser(TempUser tempUser) {
        try {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client.setEmail(tempUser.getEmail());
            client.setName(tempUser.getName());
            client.setSurname(tempUser.getSurname());
            client.setPhone(tempUser.getPhone());
            restTemplate.put(clientPath, client, Client.class);
        }catch (Exception e) {
            try {
                Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                seller.setEmail(tempUser.getEmail());
                seller.setName(tempUser.getName());
                seller.setSurname(tempUser.getSurname());
                seller.setPhone(tempUser.getPhone());
                restTemplate.put(sellerPath, seller, Seller.class);
            }catch (Exception e1) { throw new IllegalStateException(e1); }
        }
    }

    // Send request to update user password
    @Override
    public void changePassword(TempUser tempUser) {
        try {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client.setPassword(passwordEncoder.encode(tempUser.getPassword()));
            restTemplate.postForEntity(clientPath, client, Client.class);
        }catch (Exception e) {
            try {
                Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                seller.setPassword(passwordEncoder.encode(tempUser.getPassword()));
                restTemplate.postForEntity(sellerPath, seller, Seller.class);
            }catch (Exception e1) { throw new IllegalStateException(e1); }
        }
    }

    // Find user by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = getAllClients().stream().filter(c -> c.getEmail().equals(email)).findFirst();
        if (client.isPresent()) return client.get();
        Optional<Seller> seller = getAllSellers().stream().filter(s -> s.getEmail().equals(email)).findFirst();
        if (seller.isPresent()) return seller.get();
        else throw new IllegalStateException("User not found");
    }
}

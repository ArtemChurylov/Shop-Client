package com.example.shopclient.security.service;

import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.TempUser;
import com.example.shopclient.security.model.Seller;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveClient(TempUser tempUser);
    List<Client> getAllClients();
    Client getClientById(Long id);
    void deleteClientById(Long id);

    void saveSeller(TempUser tempUser);
    List<Seller> getAllSellers();
    Seller getSellerById(Long id);
    void deleteSellerById(Long id);
}

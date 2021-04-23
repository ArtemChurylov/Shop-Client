package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Notification;
import com.example.shopclient.security.model.Seller;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications();
    void saveNotification(Notification notification);
    void deleteNotification(Long id, Seller seller);

    List<Notification> getMyNotifications(Long id);
}

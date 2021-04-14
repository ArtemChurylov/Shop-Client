package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications();
    void saveNotification(Notification notification);
    void deleteNotification(Long id);
}

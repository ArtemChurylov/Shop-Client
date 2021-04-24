package com.example.shopclient.application.service;

import com.example.shopclient.application.model.Notification;
import com.example.shopclient.security.model.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final static String notificationPath = "https://shop-server-artem.herokuapp.com/service/notification";
    private final static String notificationPathWithId = "https://shop-server-artem.herokuapp.com/service/notification/{id}";

    private final RestTemplate restTemplate = new RestTemplate();

    // Send request to server for getting all notifications
    @Override
    public List<Notification> getAllNotifications() {
        ResponseEntity<Notification[]> responseEntity = restTemplate.getForEntity(notificationPath, Notification[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    // Send request to server to save notification
    @Override
    public void saveNotification(Notification notification) {
        restTemplate.postForEntity(notificationPath, notification, Notification.class);
    }

    // Send request to delete notification
    @Override
    public void deleteNotification(Long id, Seller seller) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        if (getAllNotifications().stream().anyMatch(notification -> notification.getId() == id)) {
            // Decrease notification count to display correct value without relogin
            seller.setNotificationCount(seller.getNotificationCount()-1);
            restTemplate.delete(notificationPathWithId, map);
        }
    }

    // Send request to get all seller notifications
    @Override
    public List<Notification> getMyNotifications(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Notification[]> responseEntity =
                restTemplate.getForEntity(notificationPathWithId+"/myNotifications", Notification[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }
}

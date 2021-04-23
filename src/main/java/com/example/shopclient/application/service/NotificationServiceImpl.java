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

    private final static String notificationPath = "http://localhost:8080/service/notification";
    private final static String notificationPathWithId = "http://localhost:8080/service/notification/{id}";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Notification> getAllNotifications() {
        ResponseEntity<Notification[]> responseEntity = restTemplate.getForEntity(notificationPath, Notification[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public void saveNotification(Notification notification) {
        restTemplate.postForEntity(notificationPath, notification, Notification.class);
    }

    @Override
    public void deleteNotification(Long id, Seller seller) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        if (getAllNotifications().stream().anyMatch(notification -> notification.getId() == id)) {
            seller.setNotificationCount(seller.getNotificationCount()-1);
            restTemplate.delete(notificationPathWithId, map);
        }
    }

    @Override
    public List<Notification> getMyNotifications(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        ResponseEntity<Notification[]> responseEntity =
                restTemplate.getForEntity(notificationPathWithId+"/myNotifications", Notification[].class, map);
        return Arrays.asList(responseEntity.getBody());
    }
}

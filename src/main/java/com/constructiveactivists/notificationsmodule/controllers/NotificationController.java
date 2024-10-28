package com.constructiveactivists.notificationsmodule.controllers;

import com.constructiveactivists.notificationsmodule.controllers.configuration.NotificationAPI;
import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import com.constructiveactivists.notificationsmodule.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.notification}")
public class NotificationController implements NotificationAPI {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<List<NotificationEntity>> getUserNotifications(@PathVariable Integer userId) {
        List<NotificationEntity> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @Override
    public ResponseEntity<List<NotificationEntity>> getMostRecentUserNotifications(@PathVariable Integer userId) {
        List<NotificationEntity> notifications = notificationService.getMostRecentFiveNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}

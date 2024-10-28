package com.constructiveactivists.notificationsmodule.controllers;

import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import com.constructiveactivists.notificationsmodule.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserNotifications_ShouldReturnNotifications() {
        Integer userId = 1;
        List<NotificationEntity> notifications = List.of(new NotificationEntity());

        when(notificationService.getNotificationsByUserId(userId)).thenReturn(notifications);

        ResponseEntity<List<NotificationEntity>> response = notificationController.getUserNotifications(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notifications, response.getBody());
        verify(notificationService, times(1)).getNotificationsByUserId(userId);
    }

    @Test
    void getMostRecentUserNotifications_ShouldReturnMostRecentNotifications() {
        Integer userId = 1;
        List<NotificationEntity> notifications = List.of(new NotificationEntity());

        when(notificationService.getMostRecentFiveNotificationsByUserId(userId)).thenReturn(notifications);

        ResponseEntity<List<NotificationEntity>> response = notificationController.getMostRecentUserNotifications(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notifications, response.getBody());
        verify(notificationService, times(1)).getMostRecentFiveNotificationsByUserId(userId);
    }
}

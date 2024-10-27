package com.constructiveactivists.notificationsmodule.repositories;

import com.constructiveactivists.notificationsmodule.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByUserId(Integer userId);
}

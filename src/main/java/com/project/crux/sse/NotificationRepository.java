package com.project.crux.sse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Optional<Notification> findById(Long NotificationsId);

    void deleteById(Long notificationId);
}

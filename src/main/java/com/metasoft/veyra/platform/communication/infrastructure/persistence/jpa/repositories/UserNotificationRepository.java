package com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<UserNotification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, NotificationStatus status);

    Optional<UserNotification> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndStatus(Long userId, NotificationStatus status);

    List<UserNotification> findByUserIdAndStatus(Long userId, NotificationStatus status);
}

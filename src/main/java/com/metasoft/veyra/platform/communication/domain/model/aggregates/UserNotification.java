package com.metasoft.veyra.platform.communication.domain.model.aggregates;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserNotification extends AuditableAbstractAggregateRoot<UserNotification> {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    @Column
    private LocalDateTime readAt;

    protected UserNotification() {
    }

    public UserNotification(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.status = NotificationStatus.UNREAD;
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }
}

package com.metasoft.veyra.platform.communication.domain.model.aggregates;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
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
public class UserPushToken extends AuditableAbstractAggregateRoot<UserPushToken> {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PushPlatform platform;

    @Column(nullable = false)
    private LocalDateTime lastSeenAt;

    protected UserPushToken() {
    }

    public UserPushToken(Long userId, String token, PushPlatform platform) {
        this.userId = userId;
        this.token = token;
        this.platform = platform;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void refresh(PushPlatform platform) {
        this.platform = platform;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void reassignTo(Long userId, PushPlatform platform) {
        this.userId = userId;
        refresh(platform);
    }
}

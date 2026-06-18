package com.metasoft.veyra.platform.communication.application.internal.queryservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadNotificationCountQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserNotificationsQuery;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationQueryService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationQueryServiceImpl implements UserNotificationQueryService {

    private final UserNotificationRepository userNotificationRepository;
    private final ExternalIamService externalIamService;

    public UserNotificationQueryServiceImpl(
            UserNotificationRepository userNotificationRepository,
            ExternalIamService externalIamService
    ) {
        this.userNotificationRepository = userNotificationRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<UserNotification> handle(GetUserNotificationsQuery query) {
        externalIamService.ensureUserExists(query.userId());

        if (query.status() == null) {
            return userNotificationRepository.findByUserIdOrderByCreatedAtDesc(query.userId());
        }

        return userNotificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(query.userId(), query.status());
    }

    @Override
    public long handle(GetUnreadNotificationCountQuery query) {
        externalIamService.ensureUserExists(query.userId());
        return userNotificationRepository.countByUserIdAndStatus(query.userId(), NotificationStatus.UNREAD);
    }
}

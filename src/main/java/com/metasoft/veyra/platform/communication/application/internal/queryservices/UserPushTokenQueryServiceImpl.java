package com.metasoft.veyra.platform.communication.application.internal.queryservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserPushTokensQuery;
import com.metasoft.veyra.platform.communication.domain.services.UserPushTokenQueryService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserPushTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPushTokenQueryServiceImpl implements UserPushTokenQueryService {

    private final UserPushTokenRepository userPushTokenRepository;
    private final ExternalIamService externalIamService;

    public UserPushTokenQueryServiceImpl(
            UserPushTokenRepository userPushTokenRepository,
            ExternalIamService externalIamService
    ) {
        this.userPushTokenRepository = userPushTokenRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<UserPushToken> handle(GetUserPushTokensQuery query) {
        externalIamService.ensureUserExists(query.userId());
        return userPushTokenRepository.findByUserId(query.userId());
    }
}

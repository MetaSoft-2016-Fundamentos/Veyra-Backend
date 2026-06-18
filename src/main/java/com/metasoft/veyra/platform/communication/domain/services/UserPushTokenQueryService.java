package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserPushTokensQuery;

import java.util.List;

public interface UserPushTokenQueryService {
    List<UserPushToken> handle(GetUserPushTokensQuery query);
}

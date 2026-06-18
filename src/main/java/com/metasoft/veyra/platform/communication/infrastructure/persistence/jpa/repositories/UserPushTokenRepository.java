package com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPushTokenRepository extends JpaRepository<UserPushToken, Long> {
    List<UserPushToken> findByUserId(Long userId);

    Optional<UserPushToken> findByToken(String token);

    Optional<UserPushToken> findByUserIdAndToken(Long userId, String token);

    void deleteByUserIdAndToken(Long userId, String token);
}

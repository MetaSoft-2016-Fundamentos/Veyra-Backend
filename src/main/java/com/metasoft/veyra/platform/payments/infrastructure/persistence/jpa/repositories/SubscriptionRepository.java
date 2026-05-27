package com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    List<Subscription> findByUserId(UserId userId);

    Optional<Subscription> findByUserIdAndStatus(UserId userId, SubscriptionStatus status);
    boolean existsByUserIdAndStatus(UserId userId, SubscriptionStatus status);
}
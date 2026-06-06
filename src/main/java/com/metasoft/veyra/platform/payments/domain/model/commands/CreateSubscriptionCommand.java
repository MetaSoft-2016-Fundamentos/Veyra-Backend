package com.metasoft.veyra.platform.payments.domain.model.commands;
public record CreateSubscriptionCommand(Long userId, String planType, String period, String paymentMethodId
) {
    public CreateSubscriptionCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (planType ==null|| planType.isBlank()){
            throw new IllegalArgumentException("Play Type cannot be null or blank");
        }
        if (period==null||period.isBlank()){
            throw new IllegalArgumentException("Period cannot be null or blank");
        }
        if (paymentMethodId==null||paymentMethodId.isBlank()){
            throw new IllegalArgumentException("Period cannot be null or blank");
        }
    }
}
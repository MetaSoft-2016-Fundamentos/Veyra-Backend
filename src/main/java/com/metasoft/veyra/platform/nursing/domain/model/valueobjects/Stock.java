package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;
import jakarta.persistence.Embeddable;
@Embeddable
public record Stock(Integer amount) {
    public Stock {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Stock cannot be null or negative");
        }
    }
    public Stock decrease(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to decrease must be positive");
        }
        if (amount - quantity < 0) {
            throw new IllegalStateException(
                    "Not enough stock available. Current: " + amount + ", Requested: " + quantity
            );
        }
        return new Stock(amount - quantity);
    }

    public Stock increase(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be positive");
        }
        return new Stock(amount + quantity);
    }


    public boolean isEmpty() {
        return amount == 0;
    }
    public boolean isLow(int threshold) {
        return amount <= threshold;
    }
    public boolean hasEnough(int quantity) {
        return amount >= quantity;
    }

}

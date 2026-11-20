package shop;

import java.util.Objects;

public record OrderLine(String productId, Money unitPrice, int quantity) {

    public OrderLine {
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }

    public Money total() {
        return unitPrice.multiply(quantity);
    }
}

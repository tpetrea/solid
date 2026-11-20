package shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * Invariants:
 *  - every line uses the order's currency
 *  - total == sum of line totals
 *  - lines can only change while status is NEW
 *  - status only moves along the allowed transitions
 */
public class Order {

    private final String id;
    private final Currency currency;
    private final List<OrderLine> lines = new ArrayList<>();
    private Money total;
    private OrderStatus status = OrderStatus.NEW;

    public Order(String id, Currency currency) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(currency, "currency");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id;
        this.currency = currency;
        this.total = new Money(BigDecimal.ZERO, currency);
    }

    public void addLine(OrderLine line) {
        Objects.requireNonNull(line, "line");
        requireStatus(OrderStatus.NEW);

        // Everything that can fail happens first...
        Money newTotal = total.add(line.total());   // throws on currency mismatch

        // ...state changes only after it all succeeded (strong exception guarantee).
        lines.add(line);
        total = newTotal;
    }

    public void submit() {
        requireStatus(OrderStatus.NEW);
        if (lines.isEmpty()) {
            throw new IllegalStateException("cannot submit an empty order");
        }
        status = OrderStatus.PENDING;
    }

    public void confirm() {
        requireStatus(OrderStatus.PENDING);
        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == OrderStatus.CONFIRMED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("cannot cancel an order that is " + status);
        }
        status = OrderStatus.CANCELLED;
    }

    private void requireStatus(OrderStatus expected) {
        if (status != expected) {
            throw new IllegalStateException("expected " + expected + " but was " + status);
        }
    }

    public String id()           { return id; }
    public Currency currency()   { return currency; }
    public Money total()         { return total; }
    public OrderStatus status()  { return status; }
    public List<OrderLine> lines() { return List.copyOf(lines); }   // callers can't modify ours
}

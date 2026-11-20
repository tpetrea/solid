package shop;

public enum OrderStatus {
    NEW,        // lines can be added
    PENDING,    // submitted, waiting for payment
    CONFIRMED,  // payment succeeded
    CANCELLED   // cancelled by the customer or because payment failed
}

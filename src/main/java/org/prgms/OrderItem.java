package org.prgms;

import java.util.UUID;

public class OrderItem {
    public final UUID prductId;
    public final long productPrice;
    public final long quantity;

    public OrderItem(UUID prductId, long productPrice, int quantity) {
        this.prductId = prductId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public UUID getPrductId() {
        return prductId;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}

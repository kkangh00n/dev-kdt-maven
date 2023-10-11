package org.prgms;

import java.util.List;
import java.util.UUID;

public class Order {

    private final UUID orderId;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private long discountAmount;
    private OrderStatus orderStatus = OrderStatus.ACCEPTED;

    public Order(UUID orderId, UUID customerId, List<OrderItem> orderItems, long discountAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.discountAmount = discountAmount;
    }

    public long totalAmount(){
        Long beforeDiscount = orderItems.stream().map(v -> v.getProductPrice() * v.getQuantity())
            .reduce(0L, Long::sum);

        return beforeDiscount - discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

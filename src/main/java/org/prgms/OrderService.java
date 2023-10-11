package org.prgms;

import java.util.List;
import java.util.UUID;

public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems){
        Voucher voucher = voucherService.getVoucher(voucherId);
        Order order = new Order(UUID.randomUUID(), customerId, orderItems);
        orderRepository.insert(order);

        return order;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId){
        Voucher voucher = voucherService.getVoucher(voucherId);
        Order order = new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        orderRepository.insert(order);
        voucherService.useVoucher(voucher);

        return order;
    }


}

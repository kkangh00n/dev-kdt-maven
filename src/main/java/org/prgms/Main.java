package org.prgms;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;
import org.prgms.order.Order;
import org.prgms.order.OrderItem;
import org.prgms.order.OrderService;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            AppConfiguration.class);

        UUID customerId = UUID.randomUUID();

        VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order = orderService.createOrder(customerId, new ArrayList<>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount() == 90L,
            MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));

    }
}
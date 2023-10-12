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
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            AppConfiguration.class);

        UUID customerId = UUID.randomUUID();
        VoucherRepository voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
            applicationContext.getBeanFactory(), VoucherRepository.class, "memory");
        VoucherRepository voucherRepository2 = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
            applicationContext.getBeanFactory(), VoucherRepository.class, "memory");

        System.out.println(MessageFormat.format("voucherRepository {0}", voucherRepository));
        System.out.println(MessageFormat.format("voucherRepository2 {0}", voucherRepository2));
        System.out.println(voucherRepository==voucherRepository2);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order = orderService.createOrder(customerId, new ArrayList<>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount() == 90L,
            MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));

    }
}
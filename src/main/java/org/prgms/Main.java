package org.prgms;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.prgms.order.Order;
import org.prgms.order.OrderItem;
import org.prgms.order.OrderProperties;
import org.prgms.order.OrderService;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.JdbcVoucherRepository;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AppConfiguration.class);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("dev");
        applicationContext.refresh();

//        String version = environment.getProperty("kdt.version");
//        Integer minimumOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class);
//        List<String> supportVendors = environment.getProperty("kdt.support-vendors", List.class);
//        List<String> description = environment.getProperty("kdt.description", List.class);
//        System.out.println(MessageFormat.format("version -> {0}", version));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", minimumOrderAmount));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", supportVendors));
//        System.out.println(MessageFormat.format("description -> {0}", description));

//        OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
//        System.out.println(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
//        System.out.println(MessageFormat.format("description -> {0}", orderProperties.getDescription()));

        UUID customerId = UUID.randomUUID();
        VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));

        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order = orderService.createOrder(customerId, new ArrayList<OrderItem>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount() == 90L,
            MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));

        applicationContext.close();
    }
}
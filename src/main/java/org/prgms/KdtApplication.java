package org.prgms;

import java.text.MessageFormat;
import java.util.UUID;
import org.prgms.order.OrderProperties;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.JdbcVoucherRepository;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgms.order", "org.prgms.voucher", "org.prgms.configuration"})
public class KdtApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(KdtApplication.class);
        springApplication.setAdditionalProfiles("local");
        ConfigurableApplicationContext applicationContext = springApplication.run(args);

        OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
        System.out.println(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
        System.out.println(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
        System.out.println(MessageFormat.format("description -> {0}", orderProperties.getDescription()));

        UUID customerId = UUID.randomUUID();
        VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));

    }
}

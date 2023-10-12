package org.prgms;

import java.util.Optional;
import java.util.UUID;
import org.prgms.order.Order;
import org.prgms.voucher.Voucher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.prgms.order", "org.prgms.voucher"})
//@ComponentScan(basePackageClasses = {Order.class, Voucher.class})
public class AppConfiguration {
}

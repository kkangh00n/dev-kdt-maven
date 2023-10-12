package org.prgms;

import java.util.Optional;
import java.util.UUID;
import org.prgms.order.Order;
import org.prgms.voucher.Voucher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.prgms.order", "org.prgms.voucher"})
//@ComponentScan(basePackageClasses = {Order.class, Voucher.class})
@PropertySource("application.properties")
public class AppConfiguration {

    @Bean(initMethod = "init")
    public BeanOne beanOne(){
        return new BeanOne();
    }

}

class BeanOne implements InitializingBean{

    public void init(){
        System.out.println("[BeanOne] init called");
    }


    @Override
    public void afterPropertiesSet(){
        System.out.println("[BeanOne] afterPropertiesSet called");
    }
}

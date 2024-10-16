package org.prgms;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.prgms.order.Order;
import org.prgms.order.OrderItem;
import org.prgms.order.OrderProperties;
import org.prgms.order.OrderService;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.JdbcVoucherRepository;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class Main {

    //이 Logger는 해당 클래스에 단 하나
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        AnsiOutput.setEnabled(Enabled.ALWAYS);
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

        OrderProperties orderProperties = applicationContext.getBean(OrderProperties.class);
        logger.info("logger name -> {} {} {}", logger.getName(), 2, 3);
        logger.info("version -> {}", orderProperties.getVersion());
        logger.info("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.info("supportVendors -> {}", orderProperties.getSupportVendors());
        logger.info("description -> {}", orderProperties.getDescription());


//        Resource resource = applicationContext.getResource("application.yaml");
//        Resource resource2 = applicationContext.getResource("file:sample.txt");
//
//        System.out.println("resource = " + resource.getClass().getCanonicalName());
//        File file = resource.getFile();
//        List<String> strings = Files.readAllLines(file.toPath());
//        System.out.println(strings.stream().reduce("", (a,b) -> a+"\n"+b));



//        Resource resource3 = applicationContext.getResource("https://stackoverflow.com/");
//
//        InputStream inputStream = resource3.getURL().openStream();
//        ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
//        BufferedReader bufferedReader = new BufferedReader(
//            Channels.newReader(readableByteChannel, StandardCharsets.UTF_8));
//        Stream<String> lines = bufferedReader.lines();
//        String content = lines.collect(Collectors.joining("\n"));
//        System.out.println(content);



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
package org.prgms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.order.Order;
import org.prgms.order.OrderItem;
import org.prgms.order.OrderService;
import org.prgms.order.OrderStatus;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

//Spring TestContext를 사용하도록 설정
@SpringJUnitConfig
//profile 설정
@ActiveProfiles("local")
public class KdtSpringContextTests {

    //컴포넌트 스캔을 통해 bean 등록
    @Configuration
    @ComponentScan(basePackages = {"org.prgms.voucher", "org.prgms.order"})
    static class Config{
    }

    @Autowired
    ApplicationContext context;

    //의존 관계가 형성된 상태에서 OrderService가 생성되어 주입
    @Autowired
    OrderService orderService;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("applicationContext가 생성")
    void testApplicationContext() {
        assertThat(context, notNullValue());
    }

    @Test
    @DisplayName("VoucherRepository가 빈으로 등록되어 있어야 한다.")
    void testVoucherRepositoryCreation() {
        VoucherRepository bean = context.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
    }

    @Test
    @DisplayName("통합테스트 진행, 실제 빈으로 등록된 객체 간에 의존관계를 테스트")
    void testOrderService() {
        //given
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        //when
        Order order = orderService.createOrder(UUID.randomUUID(),
            List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }
}

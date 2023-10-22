package org.prgms.aop;


import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.MemoryVoucherRepository;
import org.prgms.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@ActiveProfiles("local")
public class AopTests {

    //aop 클래스 파일 bean 등록
    @Configuration
    @EnableAspectJAutoProxy
    @ComponentScan(basePackages = {"org.prgms.voucher", "org.prgms.aop"})
    static class Config{
    }

    @Autowired
    ApplicationContext context;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("Aop Test")
    void testOrderService() {
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        //context에서 가져온 bean 객체가 아니기 때문에 aop 적용 x
        VoucherRepository voucherRepository1 = new MemoryVoucherRepository();
        voucherRepository1.insert(fixedAmountVoucher);
    }
}

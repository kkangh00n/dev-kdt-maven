package org.prgms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.voucher.Voucher;
import org.prgms.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

//Spring TestContext를 사용하도록 설정
@SpringJUnitConfig
public class KdtSpringContextTests {

    @Configuration
    static class Config{
        @Bean
        VoucherRepository voucherRepository(){
            return new VoucherRepository() {
                @Override
                public Optional<Voucher> findById(UUID voucherId) {
                    return Optional.empty();
                }

                @Override
                public Voucher insert(Voucher voucher) {
                    return null;
                }
            };

        }
    }

    @Autowired
    ApplicationContext context;

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
}

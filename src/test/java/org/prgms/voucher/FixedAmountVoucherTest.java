package org.prgms.voucher;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FixedAmountVoucherTest {

    private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);

    @BeforeAll
    static void setup(){
        logger.info("@BeforeAll - run once");
    }

    @BeforeEach
    void init(){
        logger.info("@BeforeEach - run before each test method");
    }

    @Test
    @DisplayName("기본적인 assertEqual 테스트")
    void testAssertEqual() {
        assertEquals(2, 1+1);
    }

    @Test
    void testDiscount() {
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, fixedAmountVoucher.discount(1000));
    }

    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    @Disabled     //테스트 제외 가능
    void testWithMinus() {
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }
}
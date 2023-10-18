package org.prgms.voucher;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.nio.sctp.IllegalReceiveException;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FixedAmountVoucherTest {

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
    @Disabled     //잠시 테스트 제외 가능
    void testWithMinus() {
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }
}
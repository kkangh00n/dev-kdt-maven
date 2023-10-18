package org.prgms.voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher{
    private static final long MAX_VOUCHER_AMOUNT = 10000;

    private final UUID voucherId;
    private final long amount;

    public FixedAmountVoucher(UUID voucherId, long amount) {
        if(amount<0) throw new IllegalArgumentException("음수는 될 수 없음");
        if(amount==0) throw new IllegalArgumentException("0이 될 수 없음");
        if(amount>MAX_VOUCHER_AMOUNT) throw new IllegalArgumentException("%d보다 작아야 함".formatted(MAX_VOUCHER_AMOUNT));
        this.voucherId = voucherId;
        this.amount = amount;
    }

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    public long discount(long beforeDiscount){
        long discountAmount = beforeDiscount - amount;
        return discountAmount<0?0:discountAmount;
    }
}

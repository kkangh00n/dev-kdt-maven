package org.prgms;

import java.util.UUID;

public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher getVoucher(UUID voucherId){
        return voucherRepository
            .findById(voucherId)
            .orElseThrow(()->new RuntimeException("Voucher 찾을 수 없음"));
    }

    public void useVoucher(Voucher voucher){

    }
}

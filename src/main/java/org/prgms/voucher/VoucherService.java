package org.prgms.voucher;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(@Qualifier("memory") VoucherRepository voucherRepository) {
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

package org.prgms.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.prgms.voucher.FixedAmountVoucher;
import org.prgms.voucher.MemoryVoucherRepository;
import org.prgms.voucher.VoucherService;

class OrderServiceTest {

    //stub
    class OrderRepositoryStub implements OrderRepository{
        @Override
        public Order insert(Order order) {
            return null;
        }
    }

    @Test
    @DisplayName("Order 객체 생성 (stub)")
    void createOrder() {
        //given
        MemoryVoucherRepository voucherRepository = new MemoryVoucherRepository();
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
        OrderService sut = new OrderService(new VoucherService(voucherRepository),
            new OrderRepositoryStub());

        //when
        Order order = sut.createOrder(UUID.randomUUID(),
            List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    @DisplayName("Order 객체 생성 (mock)")
    void createOrderByMock() {
        //given
        VoucherService voucherServiceMock = mock(VoucherService.class);
        OrderRepository orderRepositoryMock = mock(OrderRepository.class);
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);

        OrderService sut = new OrderService(voucherServiceMock, orderRepositoryMock);

        //when
        Order order = sut.createOrder(
            UUID.randomUUID(),
            List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
            fixedAmountVoucher.getVoucherId());

        //then
        //상태 검증
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));

        //특정한 순서를 보장한다.
        InOrder inOrder = inOrder(voucherServiceMock);
        //행위 검증
        inOrder.verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());
        verify(orderRepositoryMock).insert(order);
        inOrder.verify(voucherServiceMock).useVoucher(fixedAmountVoucher);
    }
}
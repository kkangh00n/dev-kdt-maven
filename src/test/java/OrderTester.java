import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.prgms.FixedAmountVoucher;
import org.prgms.Order;
import org.prgms.OrderItem;

public class OrderTester {

    @Test
    void test() {
        UUID customerId = UUID.randomUUID();
        List<OrderItem> orderItems = new ArrayList<>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }};

        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 10L);

        Order order = new Order(UUID.randomUUID(), customerId, orderItems, fixedAmountVoucher);

        assertThat(order.totalAmount()).isEqualTo(order.totalAmount());

    }
}

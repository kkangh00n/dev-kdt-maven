import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.prgms.FixedAmountVoucher;
import org.prgms.Order;
import org.prgms.OrderContext;
import org.prgms.OrderItem;
import org.prgms.OrderService;

public class OrderTester {

    @Test
    void test() {
        UUID customerId = UUID.randomUUID();
        OrderContext orderContext = new OrderContext();
        OrderService orderService = orderContext.orderService();
        Order order = orderService.createOrder(customerId, new ArrayList<>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }});

        assertThat(order.totalAmount()).isEqualTo(order.totalAmount());

    }
}

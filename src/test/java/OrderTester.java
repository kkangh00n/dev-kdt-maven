import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.prgms.Order;
import org.prgms.AppConfiguration;
import org.prgms.OrderItem;
import org.prgms.OrderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderTester {

    @Test
    void test() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            AppConfiguration.class);

        UUID customerId = UUID.randomUUID();
        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order = orderService.createOrder(customerId, new ArrayList<>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }});

        assertThat(order.totalAmount()).isEqualTo(order.totalAmount());

    }
}

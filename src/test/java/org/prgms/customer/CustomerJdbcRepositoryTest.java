package org.prgms.customer;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
//해당 클래스의 인스턴스가 하나! -> @BeforeAll을 static 없이 작성 가능
@TestInstance(Lifecycle.PER_CLASS)
//OrderAnnotation을 이용하여 순서 보장
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerJdbcRepositoryTest {

    //DataSource 설정
    @Configuration
    @ComponentScan(
        basePackages = {"org.prgms.customer"}
    )
    static class Config{
        //DB 설정 정보 넘겨주기
        @Bean
        public DataSource dataSource(){
            HikariDataSource dataSource = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost/order_mgmt")
                .username("root")
                .password("0000")
                .type(HikariDataSource.class) //DB Connection pool -> HikariCP
                .build();
            dataSource.setMaximumPoolSize(1000);    //최대 Connection pool -> 1000개
            dataSource.setMinimumIdle(100);         //기본 100개로 시작하여 동작
            return dataSource;
        }
    }

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    Customer newCustomer;

    //Test 전 설정
    @BeforeAll
    void setUp(){
        newCustomer = new Customer(UUID.randomUUID(), "test-user", "test1-user@gmail.com", LocalDateTime.now());
        customerJdbcRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("HikariCP 확인")
    void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다.")
    void testInsert() {
        customerJdbcRepository.insert(newCustomer);

        Optional<Customer> retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다.")
    void testFindAll() {
        List<Customer> customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    void testFindByName() {
        Optional<Customer> customer = customerJdbcRepository.findByName(newCustomer.getName());
        assertThat(customer.isEmpty(), is(false));

        Optional<Customer> unknown = customerJdbcRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(5)
    @DisplayName("이메일로 고객을 조회할 수 있다.")
    void testFindByEmail() {
        Optional<Customer> customer = customerJdbcRepository.findByEmail(newCustomer.getEmail());
        assertThat(customer.isEmpty(), is(false));

        Optional<Customer> unknown = customerJdbcRepository.findByName("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @Order(6)
    @DisplayName("고객을 수정할 수 있다.")
    void testUpdate() {
        newCustomer.changeName("updated-user");
        customerJdbcRepository.update(newCustomer);

        List<Customer> all = customerJdbcRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all, everyItem(samePropertyValuesAs(newCustomer)));

        Optional<Customer> retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }
}
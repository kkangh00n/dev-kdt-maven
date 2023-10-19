package org.prgms.customer;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
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

    @Test
    @DisplayName("HikariCP 확인")
    void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @DisplayName("전체 고객을 조회할 수 있다.")
    void testFindAll() {
        List<Customer> customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    void testFindByName() {
        Optional<Customer> customer = customerJdbcRepository.findByName("tester00");
        assertThat(customer.isEmpty(), is(false));

        Optional<Customer> unknown = customerJdbcRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @DisplayName("이메일로 고객을 조회할 수 있다.")
    void testFindByEmail() {
        Optional<Customer> customer = customerJdbcRepository.findByEmail("test00@gmail.com");
        assertThat(customer.isEmpty(), is(false));

        Optional<Customer> unknown = customerJdbcRepository.findByName("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }

    @Test
    @DisplayName("고객을 추가할 수 있다.")
    void testInsert() {
        customerJdbcRepository.deleteAll();
        Customer newCustomer = new Customer(UUID.randomUUID(), "test-user", "test-user1@gmail.com", LocalDateTime.now());
        customerJdbcRepository.insert(newCustomer);

        Optional<Customer> retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }
}
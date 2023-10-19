package org.prgms.customer;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
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
}
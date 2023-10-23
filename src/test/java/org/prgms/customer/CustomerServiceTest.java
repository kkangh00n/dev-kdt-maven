package org.prgms.customer;

import com.zaxxer.hikari.HikariDataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Configuration
    //트랜잭션 어노테이션을 사용하기 위한 설정
    @EnableTransactionManagement
    @ComponentScan(
        basePackages = {"org.prgms.customer"}
    )
    static class Config{
        @Bean
        public DataSource dataSource(){
            HikariDataSource dataSource = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost/order_mgmt")
                .username("root")
                .password("0000")
                .type(HikariDataSource.class)
                .build();
//            dataSource.setMaximumPoolSize(1000);    //최대 Connection pool -> 1000개
//            dataSource.setMinimumIdle(100);         //기본 100개로 시작하여 동작
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate){
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager){
            return new TransactionTemplate(platformTransactionManager);
        }
    }

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

}
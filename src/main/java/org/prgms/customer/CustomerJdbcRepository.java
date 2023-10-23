package org.prgms.customer;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepository.class);

    //NamedParameterJdbcTemplate 추가
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final TransactionTemplate transactionTemplate;
    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        String customerName = resultSet.getString("name");
        String email = resultSet.getString("email");
        UUID customerId = toUUID(resultSet.getBytes("customer_id"));
        LocalDateTime lastLoginAt =
            resultSet.getTimestamp("last_login_at") != null ? resultSet.getTimestamp(
                "last_login_at").toLocalDateTime() : null;
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Customer(customerId, customerName, email, lastLoginAt, createdAt);
    };

    public CustomerJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    //customer의 정보를 map에 담아 반환
    private Map<String, Object> toParamMap(Customer customer){
        return new HashMap(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt()!=null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};
    }

    @Override
    public Customer insert(Customer customer) {
        int update = jdbcTemplate.update(
            "INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(:customerId), :name, :email, :createdAt)",
            toParamMap(customer));
        if(update!=1) throw new RuntimeException("Nothing was inserted");
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        int update = jdbcTemplate.update(
            "UPDATE customers SET name = :name, email = :email, last_login_at = :lastLoginAt where customer_id = UUID_TO_BIN(:customerId)",
            toParamMap(customer));
        if(update!=1) throw new RuntimeException("Nothing was inserted");
        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Collections.emptyMap(), Integer.class);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers where customer_id = UUID_TO_BIN(:customerId)",
                Collections.singletonMap("customerId", customerId.toString().getBytes()),
                customerRowMapper));
        } catch(EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = :name",
                Collections.singletonMap("name", name),
                customerRowMapper));
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = :email",
                Collections.singletonMap("email", email),
                customerRowMapper));
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    public void testTransaction(Customer customer){
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //로직만 간단하게 추가
                jdbcTemplate.update("UPDATE customers SET name = :name WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
                jdbcTemplate.update("UPDATE customers SET email = :email WHERE customer_id = UUID_TO_BIN(:customerId)", toParamMap(customer));
            }
        });
    }

    @Override
    public void deleteAll() {
//        jdbcTemplate.update("DELETE FROM customers", Collections.emptyMap());
        //다음과 같이 jdbcTemplate을 get하여 사용 가능
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM customers");
    }

    static UUID toUUID(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}

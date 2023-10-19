package org.prgms.customer;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepository.class);

    private final DataSource dataSource;

    //JdbcTemplate 추가
    private final JdbcTemplate jdbcTemplate;

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

    public CustomerJdbcRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer insert(Customer customer) {
        int update = jdbcTemplate.update(
            "INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)",
            customer.getCustomerId().toString().getBytes(),
            customer.getName(),
            customer.getEmail(),
            Timestamp.valueOf(customer.getCreatedAt()));
        if(update!=1) throw new RuntimeException("Nothing was inserted");
        return customer;
//        try(
//            Connection connection = dataSource.getConnection();
//            PreparedStatement statement = connection.prepareStatement("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)");
//        ) {
//            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
//            statement.setString(2, customer.getName());
//            statement.setString(3, customer.getEmail());
//            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
//            int executeUpdate = statement.executeUpdate();
//            if(executeUpdate!=1) throw new RuntimeException("Nothing was inserted");
//            return customer;
//        } catch (SQLException e) {
//            logger.error("connection closing 하는 동안 error", e);
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public Customer update(Customer customer) {
        int update = jdbcTemplate.update(
            "UPDATE customers SET name = ?, email = ?, last_login_at = ? where customer_id = UUID_TO_BIN(?)",
            customer.getName(),
            customer.getEmail(),
            customer.getLastLoginAt()!=null?Timestamp.valueOf(customer.getLastLoginAt()):null,
            customer.getCustomerId().toString().getBytes()
        );
        if(update!=1) throw new RuntimeException("Nothing was inserted");
        return customer;
//        try(
//            Connection connection = dataSource.getConnection();
//            PreparedStatement statement = connection.prepareStatement("UPDATE customers SET name = ?, email = ?, last_login_at = ? where customer_id = UUID_TO_BIN(?)");
//        ) {
//            statement.setString(1, customer.getName());
//            statement.setString(2, customer.getEmail());
//            statement.setTimestamp(3, customer.getLastLoginAt()!=null ? Timestamp.valueOf(customer.getLastLoginAt()):null);
//            statement.setBytes(4, customer.getCustomerId().toString().getBytes());
//            int executeUpdate = statement.executeUpdate();
//            if(executeUpdate!=1) throw new RuntimeException("Nothing was inserted");
//            return customer;
//        } catch (SQLException e) {
//            logger.error("connection closing 하는 동안 error", e);
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public int count() {
        //queryForObject() 메서드는 기본적으로 단건을 조회할 때 사용하는 메서드
        return jdbcTemplate.queryForObject("select count(*) from customers", Integer.class);
    }

    //JdbcTemplate은 Jdbc에서 중첩된 코드를 템플릿화
    //다음과 같이 한 줄로 변화
    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
//        List<Customer> allCustomers = new ArrayList<>();
//        try(
//            Connection connection = dataSource.getConnection();
//            PreparedStatement statement = connection.prepareStatement("select * from customers");
//            ResultSet resultSet = statement.executeQuery();
//        ) {
//            while(resultSet.next()){
//                mapToCustomer(resultSet, allCustomers);
//            }
//        } catch (SQLException e) {
//            logger.error("Gor error while closing connection", e);
//            throw new RuntimeException(e);
//        }
//        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        try{        //queryForObject() 메서드는 기본적으로 단건을 조회할 때 사용하는 메서드
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers Where customer_id = UUID_TO_BIN(?)",
                customerRowMapper,
                customerId.toString().getBytes()));
        } catch(EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
//        List<Customer> allCustomers = new ArrayList<>();
//
//        try(
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
//            PreparedStatement statement = connection.prepareStatement("select * from customers WHERE customer_id = UUID_TO_BIN(?)");          //prepareStatement -> SQL injection 방지
//        ) {
//            statement.setBytes(1, customerId.toString().getBytes());
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    mapToCustomer(resultSet, allCustomers);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Gor error while closing connection", e);
//            throw new RuntimeException(e);
//        }
//
//        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = ?",
                customerRowMapper,
                name));
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
//        List<Customer> allCustomers = new ArrayList<>();
//
//        try(
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
//            PreparedStatement statement = connection.prepareStatement("select * from customers WHERE name = ?");          //prepareStatement -> SQL injection 방지
//        ) {
//            statement.setString(1, name);
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    mapToCustomer(resultSet, allCustomers);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Gor error while closing connection", e);
//            throw new RuntimeException(e);
//        }
//
//        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = ?",
                customerRowMapper,
                email));
        } catch (EmptyResultDataAccessException e){
            logger.error("Got empty result", e);
            return Optional.empty();
        }
//        List<Customer> allCustomers = new ArrayList<>();
//
//        try(
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
//            PreparedStatement statement = connection.prepareStatement("select * from customers WHERE email = ?");          //prepareStatement -> SQL injection 방지
//        ) {
//            statement.setString(1, email);
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    mapToCustomer(resultSet, allCustomers);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Gor error while closing connection", e);
//            throw new RuntimeException(e);
//        }
//
//        return allCustomers.stream().findFirst();
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM customers");
//        try(
//            Connection connection = dataSource.getConnection();
//            PreparedStatement statement = connection.prepareStatement("DELETE from customers");
//        ) {
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Gor error while closing connection", e);
//            throw new RuntimeException(e);
//        }
    }

    static UUID toUUID(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}

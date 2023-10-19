package org.prgms.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {

    private static final Logger loggger = LoggerFactory.getLogger(CustomerJdbcRepository.class);

    private final DataSource dataSource;

    public CustomerJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer insert(Customer customer) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from customers");
            ResultSet resultSet = statement.executeQuery();
            ) {
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                UUID customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                LocalDateTime lastLoginAt = resultSet.getTimestamp("last_login_at") != null ? resultSet.getTimestamp(
                        "last_login_at").toLocalDateTime() : null;
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                allCustomers.add(new Customer(customerId, name, email, lastLoginAt, createdAt));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}

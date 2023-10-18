package org.prgms;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers WHERE name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private final String UPDATE_NAME_BY_ID_SQL = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BIN(?)";
    private final String DELETE_ALL_SQL = "DELETE FROM customers";

    public List<String> findNames(String name){
        List<String> names = new ArrayList<>();

        try(        //DB 커넥션은 많은 resource를 차지
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_SQL);          //prepareStatement -> SQL injection 방지
        ) {
            //sql에 파라미터 전달
            statement.setString(1, name);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    String customerName = resultSet.getString("name");
                    UUID customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    LocalDateTime createAt = resultSet.getTimestamp("create_at").toLocalDateTime();
                    logger.info("customer id -> {}, name -> {}, created_at -> {}", customerId, name, createAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }

        return names;
    }

    public List<String> findAllName(){
        List<String> names = new ArrayList<>();

        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);          //prepareStatement -> SQL injection 방지
        ) {
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    String customerName = resultSet.getString("name");
                    UUID customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    LocalDateTime createAt = resultSet.getTimestamp("create_at").toLocalDateTime();
//                    logger.info("customer id -> {}, name -> {}, created_at -> {}", customerId, customerName, createAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }

        return names;
    }

    public List<UUID> findAllIds(){
        List<UUID> uuids = new ArrayList<>();

        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);          //prepareStatement -> SQL injection 방지
        ) {
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    String customerName = resultSet.getString("name");
                    //get한 UUID의 값의 버전을 바꾸어 줌
                    UUID customerId = toUUID(resultSet.getBytes("customer_id"));
                    LocalDateTime createAt = resultSet.getTimestamp("create_at").toLocalDateTime();
//                    logger.info("customer id -> {}, name -> {}, created_at -> {}", customerId, customerName, createAt);
                    uuids.add(customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }

        return uuids;
    }

    //insert Query
    public int insertCustomer(UUID customerId, String name, String email){
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }
        return 0;
    }

    //update Query
    public int updateCustomerName(UUID customerId, String name){
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(UPDATE_NAME_BY_ID_SQL);
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }
        return 0;
    }

    //delete Query
    public int deleteAllCustomers(){
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SQL);
        ) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }
        return 0;
    }

    static UUID toUUID(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static void main(String[] args){
        JdbcCustomerRepository jdbcCustomerRepository = new JdbcCustomerRepository();

        //delete
        int deleteRowCount = jdbcCustomerRepository.deleteAllCustomers();
        logger.info("deleted count -> {}", deleteRowCount);

        //insert
        UUID customerId = UUID.randomUUID();
        logger.info("created customerId -> {}", customerId);

        //다음과 같이 insert 후 select 시 생뚱맞은 id가 조회된다.
        //이유는 조회 시 UUID 버전이 달라짐 (4->3 DownGrade)
        jdbcCustomerRepository.insertCustomer(customerId, "new-user", "new-user@gmail.com");
        jdbcCustomerRepository.findAllIds().forEach(i -> logger.info("Found customerId : {}", i));
    }
}

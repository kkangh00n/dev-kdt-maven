package org.prgms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    public static void main(String[] args){
        try(        //DB 커넥션은 많은 resource를 차지
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from customers");
        ) {
            while(resultSet.next()){
                String name = resultSet.getString("name");
                UUID customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                logger.info("customer id -> {}, name -> {}", customerId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("connection closing 하는 동안 error", e);
        }
    }
}

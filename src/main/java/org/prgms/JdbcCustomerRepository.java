package org.prgms;

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

    public List<String> findNames(String name){
        String SELECT_SQL = "select * from customers WHERE name = ?";
        List<String> names = new ArrayList<>();

        try(        //DB 커넥션은 많은 resource를 차지
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "0000");
            PreparedStatement statement = connection.prepareStatement(SELECT_SQL);          //prepareStatement -> SQL injection 방지
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

    public static void main(String[] args){
        List<String> names = new JdbcCustomerRepository().findNames("tester01");
        names.forEach(v -> logger.info("Found name : {}", v));
    }
}

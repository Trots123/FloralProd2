package com.flora.repository;

import com.flora.entity.product_flor;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestRepository {
    //объявили объект HikariDataSource по имени dataSource , для общения с БД
    private final HikariDataSource dataSource;
    // тоже самое с JdbcTemplate
    private final JdbcTemplate jdbcTemplate;

    //конструктор класса
    public TestRepository() {

        //создается объект HikariDataSource и записывается в dataSource
        this.dataSource = new HikariDataSource();

        // тут мы говорим Hikari подключиться по урлу к PostgreSQL
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:6432/test_floralprod");

        dataSource.setUsername("user");

        dataSource.setPassword("password2");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // метод getAll который должен вернуть Лист продуктов флор
    public List<product_flor> getAll() {

        String test = "SELECT * FROM product_flor";
        List<product_flor> testproduct_flor = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); // Connection предоставляет подключение

             //  PreparedStatement  используется для выполнения SQL-запросов

             PreparedStatement preparedStatement = connection.prepareStatement(test);

             //ResultSet содержит результат селект
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // наш запрос
                testproduct_flor.add(
                        new product_flor(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getBigDecimal("price"),
                                resultSet.getString("description")
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

        return testproduct_flor;
    }


    public List<product_flor> getInsert(product_flor product) {

        String sql = """
            INSERT INTO product_flor (name, price, description)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getDescription());
            int rows = statement.executeUpdate();

            System.out.println("Добавлено строк: " + rows);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<product_flor> getAllAdditional() {
        String test = "SELECT * FROM product_flor";
        List<product_flor> testproduct_flor = new ArrayList<>();

        return testproduct_flor;
    }
}

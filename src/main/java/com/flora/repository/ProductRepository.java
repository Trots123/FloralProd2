package com.flora.repository;

import com.flora.entity.ProductFlor;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // возврат все продуктов
    public List<ProductFlor> getAll() {
        String sqlGetAll = """ 
                SELECT * 
                FROM product_flor 
                """;

        return jdbcTemplate.query(sqlGetAll, (rs, rowNum) ->
                new ProductFlor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock_quantity")
                )
        );
    }
    // создание продукта
    public int getInsert(ProductFlor product) {
        String sql = """
                
                    INSERT INTO product_flor (name, price, description)
                VALUES (?, ?, ?)
                """;
        int rows = jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getDescription()
        );
        return
                rows;
    }

    // поиск продукта по ID
    public Optional<ProductFlor> getById(int id) {
        String sql = """
                SELECT id, name, price, description, stock_quantity
                FROM product_flor
                WHERE id = ?
            """;
        List<ProductFlor> products = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProductFlor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock_quantity")
                ),
                id
        );

        return products.stream().findFirst();
    }

    // изменение продукта
    public int updateProduct(int id, ProductFlor product) {
        String sql = """
            UPDATE product_flor
            SET name = ?, price = ?, description = ?
            WHERE id = ?
            """;
        return jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                id
        );
    }

    //удаление продукта
    public int deleteProduct(int id) {
        String sql = """
            DELETE FROM product_flor
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, id);
    }
}


    /*
    public List<ProductFlor> getInsert(ProductFlor product) {

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

     */


/*
  private final HikariDataSource dataSource;
    // тоже самое с JdbcTemplate
    private final JdbcTemplate jdbcTemplate;

    //конструктор класса
    public ProductRepository() {

        //создается объект HikariDataSource и записывается в dataSource
        this.dataSource = new HikariDataSource();

        // тут мы говорим Hikari подключиться по урлу к PostgreSQL
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:6432/test_floralprod");

        dataSource.setUsername("user");

        dataSource.setPassword("password2");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
 // метод getAll который должен вернуть Лист продуктов флор
  public List<ProductFlor> getAll() {
 String sqlGetAll = "SELECT * FROM product_flor";
        List<ProductFlor> testproduct_flor = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); // Connection предоставляет подключение

             //  PreparedStatement  используется для выполнения SQL-запросов

             PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAll);

             //ResultSet содержит результат селект
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // наш запрос
                testproduct_flor.add(
                        new ProductFlor(
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
 */
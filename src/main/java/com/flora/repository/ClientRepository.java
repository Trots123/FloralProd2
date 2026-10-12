package com.flora.repository;

import com.flora.entity.ClientFlorr;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

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
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;


    } // Получить всех клиентов

    public List<ClientFlorr> getAllClient() {
        String sql = """ 
                SELECT * FROM client_florr 
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number") ,
                        rs.getBigDecimal("balance")));

    }

    // Найти клиента по ID public
    Optional<ClientFlorr> getByIdClient(long id) {
        String sql = """ 
                SELECT * FROM client_florr WHERE id = ? 
                """;


        List<ClientFlorr> clients = jdbcTemplate.query(sql, (rs, rowNum) ->
                new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number") ,
                        rs.getBigDecimal("balance")), id);
        return clients.stream().findFirst();

    }
    public int addClient(ClientFlorr client) {

        String sql = """
            INSERT INTO client_florr (name,  phone_number , balance)
            VALUES (?, ?, ?)
            """;

        return jdbcTemplate.update(
                sql,
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance()
        );
    }
}


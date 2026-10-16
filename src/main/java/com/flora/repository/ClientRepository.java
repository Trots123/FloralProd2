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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // поиск клиента по номеру телефона
    public Optional<ClientFlorr> findByPhoneNumber(String phoneNumber) {

        String sql = """
            SELECT id, name, phone_number, balance,
                   password_hash, created_at
            FROM client_florr
            WHERE phone_number = ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ClientFlorr client = new ClientFlorr();

            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setPhoneNumber(rs.getString("phone_number"));
            client.setBalance(rs.getBigDecimal("balance"));
            client.setPasswordHash(rs.getString("password_hash"));

            Timestamp createdAt = rs.getTimestamp("created_at");

            if (createdAt != null) {
                client.setCreatedAt(createdAt.toLocalDateTime());
            }

            return client;

            // возвращает первый подходящий элемент findFirst();
        }, phoneNumber).stream().findFirst();
    }



    // Получить всех клиентов
    public List<ClientFlorr> getAllClient() {
        String sql = """ 
                SELECT * 
                FROM client_florr 
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number") ,
                        rs.getBigDecimal("balance")));
    }



    // Найти клиента по ID public
    public Optional<ClientFlorr> getByIdClient( int id) {
        String sql = """ 
                SELECT * 
                FROM client_florr
                WHERE id = ? 
                """;
        List<ClientFlorr> clients = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getBigDecimal("balance")
                ),
                id
        );
        return clients.stream().findFirst();
    }

    public int addClient(ClientFlorr client) {

        String sql = """
            INSERT INTO client_florr
            (name, phone_number, balance, password_hash)
            VALUES (?, ?, ?, ?)
            """;

        return jdbcTemplate.update(
                sql,
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance(),
                client.getPasswordHash()
        );
    }

    // Изменение клиента
    public int updateClient(int id, ClientFlorr client) {
        String sql = """
            UPDATE client_florr
            SET name = ?, phone_number = ?, balance = ?
            WHERE id = ?
            """;
        return jdbcTemplate.update(
                sql,
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance(),
                id
        );
    }

    //удаление клиента
    public int deleteClient(int id) {
        String sql = """
            DELETE FROM client_florr
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, id);
    }
}


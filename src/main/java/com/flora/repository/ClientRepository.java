package com.flora.repository;

import com.flora.entity.ClientFlorr;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ClientFlorr> findByPhoneNumber(
            String phoneNumber
    ) {
        return jdbcTemplate.query(
                """
                SELECT id, name, phone_number,
                       balance, password_hash, created_at
                FROM client_florr
                WHERE phone_number = ?
                """,
                (rs, rowNum) -> {
                    ClientFlorr client = new ClientFlorr();

                    client.setId(rs.getInt("id"));
                    client.setName(rs.getString("name"));
                    client.setPhoneNumber(
                            rs.getString("phone_number")
                    );
                    client.setBalance(
                            rs.getBigDecimal("balance")
                    );
                    client.setPasswordHash(
                            rs.getString("password_hash")
                    );

                    Timestamp createdAt =
                            rs.getTimestamp("created_at");

                    if (createdAt != null) {
                        client.setCreatedAt(
                                createdAt.toLocalDateTime()
                        );
                    }

                    return client;
                },
                phoneNumber
        ).stream().findFirst();
    }

    public List<ClientFlorr> getAllClient() {
        return jdbcTemplate.query(
                """
                SELECT id, name, phone_number, balance
                FROM client_florr
                """,
                (rs, rowNum) -> new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getBigDecimal("balance")
                )
        );
    }

    public Optional<ClientFlorr> getByIdClient(int id) {
        return jdbcTemplate.query(
                """
                SELECT id, name, phone_number, balance
                FROM client_florr
                WHERE id = ?
                """,
                (rs, rowNum) -> new ClientFlorr(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getBigDecimal("balance")
                ),
                id
        ).stream().findFirst();
    }

    public int addClient(ClientFlorr client) {
        return jdbcTemplate.update(
                """
                INSERT INTO client_florr (
                    name, phone_number, balance, password_hash
                )
                VALUES (?, ?, ?, ?)
                """,
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance(),
                client.getPasswordHash()
        );
    }

    public int updateClient(int id, ClientFlorr client) {
        return jdbcTemplate.update(
                """
                UPDATE client_florr
                SET name = ?, phone_number = ?, balance = ?
                WHERE id = ?
                """,
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance(),
                id
        );
    }

    public int deleteClient(int id) {
        return jdbcTemplate.update(
                """
                DELETE FROM client_florr
                WHERE id = ?
                """,
                id
        );
    }

    // Пополнение баланса авторизованного клиента
    public BigDecimal topUpBalance(
            String phoneNumber,
            BigDecimal amount
    ) {
        return jdbcTemplate.query(
                """
                UPDATE client_florr
                SET balance = balance + ?
                WHERE phone_number = ?
                RETURNING id, balance
                """,
                rs -> {
                    if (!rs.next()) {
                        return null;
                    }

                    long clientId = rs.getLong("id");
                    BigDecimal newBalance =
                            rs.getBigDecimal("balance");

                    jdbcTemplate.update(
                            """
                            INSERT INTO balance_transaction (
                                client_id, amount
                            )
                            VALUES (?, ?)
                            """,
                            clientId,
                            amount
                    );

                    return newBalance;
                },
                amount,
                phoneNumber
        );
    }
}

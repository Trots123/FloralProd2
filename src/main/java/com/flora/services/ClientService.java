package com.flora.services;

import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<ClientFlorr> getClientById(int id) {
        return clientRepository.getByIdClient(id);
    }

    public List<ClientFlorr> getAllClients() {
        return clientRepository.getAllClient();
    }

    public void addClient(ClientFlorr client) {
        clientRepository.addClient(client);
    }

    public boolean updateClient(int id, ClientFlorr client) {
        return clientRepository.updateClient(id, client) > 0;
    }

    public boolean deleteClient(int id) {
        return clientRepository.deleteClient(id) > 0;
    }

    @Transactional
    public BigDecimal topUpBalance(
            String phoneNumber,
            BigDecimal amount
    ) {
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0
                || amount.scale() > 2
                || amount.compareTo(
                new BigDecimal("1000000")
        ) > 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Сумма должна быть от 0.01 до 1000000"
            );
        }

        BigDecimal newBalance =
                clientRepository.topUpBalance(
                        phoneNumber,
                        amount
                );

        if (newBalance == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Клиент не найден"
            );
        }

        return newBalance;
    }
}
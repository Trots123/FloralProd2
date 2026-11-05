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

    @Transactional
    public BigDecimal topUpBalance(String phoneNumber, BigDecimal amount) {
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0
                || amount.scale() > 2
                || amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сумма должна быть от 0.01 до 1000000");
        }

        int updated = clientRepository.increaseBalance(phoneNumber, amount);

        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Клиент не найден");
        }

        return clientRepository.findBalanceByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось получить новый баланс"));
    }
}

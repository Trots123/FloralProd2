package com.flora.services;


import com.flora.FlorDTO.RegisterRequest;
import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ClientRepository clientRepository,
                       PasswordEncoder passwordEncoder) {

        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {

        if (request == null
                || request.getName() == null
                || request.getName().isBlank()
                || request.getPhoneNumber() == null
                || request.getPhoneNumber().isBlank()
                || request.getPassword() == null
                || request.getPassword().length() < 8) {

            throw new IllegalArgumentException(
                    "Укажите имя, телефон и пароль не короче 8 символов"
            );
        }

        String phoneNumber = request.getPhoneNumber().trim();

        if (clientRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new IllegalArgumentException(
                    "Клиент с таким номером уже зарегистрирован"
            );
        }

        String passwordHash =
                passwordEncoder.encode(request.getPassword());

        ClientFlorr client = new ClientFlorr();

        client.setName(request.getName().trim());
        client.setPhoneNumber(phoneNumber);
        client.setBalance(BigDecimal.ZERO);
        client.setPasswordHash(passwordHash);

        clientRepository.addClient(client);
    }
}
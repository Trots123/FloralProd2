package com.flora.services;
import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientFlorr> getAllClients() {
        return clientRepository.getAllClient();
    }

    public void addClient(ClientFlorr client) {
        clientRepository.addClient(client);
    }
}
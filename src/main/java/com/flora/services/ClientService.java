package com.flora.services;

import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;


    }
    // поиск по ID
    public Optional<ClientFlorr> getClientById(int id) {
        return clientRepository.getByIdClient(id);
    }

    //вывод всех клинтов
    public List<ClientFlorr> getAllClients() {
        return clientRepository.getAllClient();
    }

    //создание клиента
    public void addClient(ClientFlorr client) {
        clientRepository.addClient(client);
    }

    // изменение клиента
    public boolean updateClient(int id, ClientFlorr client) {
        return clientRepository.updateClient(id, client) > 0;
    }

    //удаление клиента
    public boolean deleteClient(int id) {
        return clientRepository.deleteClient(id) > 0;
    }
}
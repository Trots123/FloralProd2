package com.flora.controller;


import com.flora.entity.ClientFlorr;
import com.flora.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // поиск клиента по ID
    @GetMapping("/{id}")
    public Optional<ClientFlorr> getClientById(@PathVariable("id") int id) {
        return clientService.getClientById(id);
    }

    // вывод всех клиентов
    @GetMapping
    public List<ClientFlorr> getAllClients() {
        return clientService.getAllClients();
    } // Добавить клиента

    // создание клиента
    @PostMapping
    public void addClient(@RequestBody ClientFlorr client) {
        clientService.addClient(client);
    }

    // Изменение клиента
    @PutMapping("/{id}")
    public boolean updateClient(
            @PathVariable("id") int id,
            @RequestBody ClientFlorr client
    ) {
        return clientService.updateClient(id, client);
    }

    // удаление клинта
    @DeleteMapping("/{id}")
    public boolean deleteClient(@PathVariable("id") int id) {
        return clientService.deleteClient(id);
    }
}
package com.flora.controller;


import com.flora.entity.ClientFlorr;
import com.flora.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    } // Получить всех клиентов

    @GetMapping
    public List<ClientFlorr> getAllClients() {
        return clientService.getAllClients();
    } // Добавить клиента

    @PostMapping
    public void addClient(@RequestBody ClientFlorr client) {
        clientService.addClient(client);
    }
}
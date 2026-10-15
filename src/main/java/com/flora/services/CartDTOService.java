package com.flora.services;

import com.flora.FlorDTO.CartDTO;
import com.flora.entity.Cart;
import com.flora.repository.CartDTORepository;
import com.flora.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartDTOService {


    private final CartDTORepository cartDTORepository;


    public CartDTOService(CartDTORepository cartDTORepository) {
        this.cartDTORepository = cartDTORepository;
    }

    //  получить все корзины для фронт
    public List<CartDTO> getForCartFrontByClientId(int clientId) {
        return cartDTORepository.getForCartFrontByClientId(clientId);
    }
}

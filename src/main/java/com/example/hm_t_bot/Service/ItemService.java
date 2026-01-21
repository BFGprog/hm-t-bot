package com.example.hm_t_bot.Service;

import org.springframework.stereotype.Service;

@Service
public interface ItemService {
    String getItemDtoRn();
    String getItemDtoId();
    public String delItem(String message);
}

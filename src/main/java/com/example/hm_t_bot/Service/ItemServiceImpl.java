package com.example.hm_t_bot.Service;

import com.example.hm_t_bot.listener.TelegramBotUpdatesListener;
import com.example.hm_t_bot.model.dto.ItemDto;
import com.example.hm_t_bot.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public String getItemDto() {
        String answer = null;
        List<ItemDto> items = itemRepository.getAllItem();
        log.info("getItemDto: {}", items.size());
        for (ItemDto i : items) {
            answer = i.getRowNum().toString() + ". "
                    + i.getName() + "\n";
        }
        return answer;
    }
}

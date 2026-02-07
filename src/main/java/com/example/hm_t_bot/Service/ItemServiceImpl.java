package com.example.hm_t_bot.Service;

import com.example.hm_t_bot.listener.TelegramBotUpdatesListener;
import com.example.hm_t_bot.model.dto.ItemDto;
import com.example.hm_t_bot.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public String getItemDtoRn() {
        StringBuilder answer = new StringBuilder();
        List<ItemDto> items = itemRepository.getAllItem();
        log.info("getItemDtoRn: {}", items.size());
        if (items.size() < 1) {
            return "0. Пусто";
        }
        for (ItemDto i : items) {
            answer
                    .append(i.getRowNum())
                    .append(". ")
                    .append(i.getName())
                    .append('\n');
        }
        return answer.toString();
    }

    @Override
    public String getItemDtoId() {
        StringBuilder answer = new StringBuilder();
        List<ItemDto> items = itemRepository.getAllItem();
        log.info("getItemDtoId: {}", items.size());
        if (items.size() < 1) {
            return "0. Пусто";
        }
        for (ItemDto i : items) {
            answer
                    .append(i.getId())
                    .append(". ")
                    .append(i.getName())
                    .append("\n");
        }
        return answer + "Перечислить номера через пробел";
    }

    /*@Override
    public String delItem(String message) {
            String ids = message.trim().replaceAll("\\s+", ",");
            itemRepository.delItemById(ids);
        return "Удалено " + ids;
    }*/

    ///
    @Override
    public String delItem(String message) {
        String[] ids = message.trim().split("\\s+");
        List<Long> idList = new ArrayList<>();
        try {
            for (String id : ids) {
                idList.add(Long.parseLong(id));
            }
        } catch (NumberFormatException e) {
            return "Ошибка. Номера через пробел (например: 1 2 3)";
        }
        return "Удалено: " + itemRepository.delItemById(idList) + " запись(-ей).";
    }

    @Override
    public String addItem(String message) {
        List<String> items = Arrays.stream(message.split("[,\\.]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        if (items.isEmpty()) {
            return "Ошибка. Напиши, например: Добавить молоко, хлеб";
        }
        itemRepository.saveItems(items);
        return "Добавлено " + items.size() + " запись(-ей).";
    }

}

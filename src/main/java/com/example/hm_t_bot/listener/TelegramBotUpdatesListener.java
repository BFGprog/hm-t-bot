package com.example.hm_t_bot.listener;

import com.example.hm_t_bot.Service.ItemService;
import com.example.hm_t_bot.Service.MessageService;
import com.example.hm_t_bot.model.enums.Status;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final MessageService messageService;
    private final ItemService itemService;


    private final String hello = "Hello";
    private final Pattern pattern = Pattern.compile("^(?s)/rec\\s(.+)$"); //("^/rec"); //("^/rec\\s+(.+)$");
    private final Pattern patternAdd = Pattern.compile("(?i)^(добавить|добавь)\\s*(.+)$");
    private final DateTimeFormatter notificationDateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(MessageService messageService, TelegramBot telegramBot, ItemService itemService) {
        this.messageService = messageService;
        this.telegramBot = telegramBot;
        this.itemService = itemService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private final Map<Long, Status> chatStatus = new ConcurrentHashMap<>();

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() == null) return;
            String message = update.message().text();
            Long chatId = update.message().chat().id();
            log.info("Processing update: {}", message);

            if (message == null) {
                messageService.answerWithMenu(chatId, "null");
                chatStatus.put(chatId, Status.DEFAULT);
            } else if (message.equals("/start")) {
                messageService.answerWithMenu(chatId, hello);
                chatStatus.put(chatId, Status.DEFAULT);
            } else if (message.equals("/item") || message.equals("Покупки")) {
                messageService.answer(chatId, itemService.getItemDtoRn());
                chatStatus.put(chatId, Status.DEFAULT);
            } else if (message.equals("/del") || message.equals("Куплено (удалить)")) {
                messageService.answer(chatId, itemService.getItemDtoId());
                chatStatus.put(chatId, Status.WAITING_ID);
            } else if (chatStatus.getOrDefault(chatId, Status.DEFAULT) == Status.WAITING_ID) {
                messageService.answer(chatId, itemService.delItem(message));
                chatStatus.put(chatId, Status.DEFAULT);
            } else if (patternAdd.matcher(message.replace("\uFEFF", "")).matches()) {
                var matcher = patternAdd.matcher(message);
                messageService.answer(chatId, itemService.addItem(matcher.group(1)));
            } else {
                messageService.answerWithMenu(chatId, message + " Не обработано");
                chatStatus.put(chatId, Status.DEFAULT);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}

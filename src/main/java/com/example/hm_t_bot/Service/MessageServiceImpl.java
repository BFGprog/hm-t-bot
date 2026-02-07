package com.example.hm_t_bot.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements MessageService {

    private TelegramBot telegramBot;

    public MessageServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void answer(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = telegramBot.execute(message);
    }
    @Override
    public void answerWithMenu(long chatId, String text) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[][]{
                        {"Покупки", "Куплено (удалить)"}
                })
                .resizeKeyboard(true)
                .oneTimeKeyboard(false);
        SendMessage message = new SendMessage(chatId, text)
                .replyMarkup(keyboard);
        telegramBot.execute(message);
    }

    @Override
    public void answerWithButton(long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Куплено")
                        .switchInlineQueryCurrentChat("/del ")
        );
        SendMessage message = new SendMessage(chatId, "Нажми Куплено и допиши номера через пробел")
                .replyMarkup(keyboard);
        telegramBot.execute(message);
    }

}

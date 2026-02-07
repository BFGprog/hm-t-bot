package com.example.hm_t_bot.Service;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {

    public void answer(Long chatId, String text);
    void answerWithMenu(long chatId, String text);
    void answerWithButton(long chatId);
}

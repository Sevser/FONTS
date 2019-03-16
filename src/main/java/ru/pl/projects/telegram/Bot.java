package ru.pl.projects.telegram;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@NoArgsConstructor
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private String name;
    private String token;

    public Bot(DefaultBotOptions options, String name, String token) {
        super(options);
        this.name = name;
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        log.info("Message from TG: {}", message);
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}

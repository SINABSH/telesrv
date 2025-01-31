package com.telesrv.bot;

import org.bukkit.Bukkit;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telesrv.plugin.MyPlugin;

public class MyTelegramBot extends TelegramLongPollingBot {

    private String botUsername = "SCP secret laboratory";  
    private String botToken = "7991087130:AAFAHiiAJyWdqLxGEDel_Fw-OFotQ_gns18";    

    private final MyPlugin plugin;

    
    public MyTelegramBot(MyPlugin plugin) {
        this.plugin = plugin;
        
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String senderName = update.getMessage().getFrom().getFirstName();

            
            if (plugin == null) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage("[Telegram] " + senderName + ": " + messageText);
            });

            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessageToTelegram(String text) {
        long chatId = -1002399038601L; // Your Telegram chat ID
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

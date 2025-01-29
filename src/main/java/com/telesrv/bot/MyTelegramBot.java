package com.telesrv.bot;

import org.bukkit.Bukkit;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telesrv.Main;

public class MyTelegramBot extends TelegramLongPollingBot {

    private String botUsername = "SCP secret laboratory";  // Replace with your bot's username
    private String botToken = "7991087130:AAFAHiiAJyWdqLxGEDel_Fw-OFotQ_gns18";    // Replace with your bot's API token

    private final Main plugin;

    // Constructor with a Main argument (used when the Main class is required)
    public MyTelegramBot(Main plugin) {
        this.plugin = plugin;
    }

    // No-argument constructor (added to resolve errors during instantiation)
    public MyTelegramBot() {
        this.plugin = null; // Set to null if not used
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void onUpdateReceived(Update update) {
        // Check if the update has a message
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();  // Get the text of the received message
            String senderName = update.getMessage().getFrom().getFirstName();  // Get the sender's name

            // Broadcast message to Minecraft chat
            if (plugin != null) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Bukkit.broadcastMessage("[Telegram] " + senderName + ": " + messageText);
                    System.out.println("Received Telegram message: " + messageText);

                });
            }

            // Optionally, send a reply back to Telegram
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Message delivered to Minecraft!");
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

    // Method for sending messages to Telegram from Minecraft
    @SuppressWarnings("CallToPrintStackTrace")
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

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}

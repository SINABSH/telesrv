package com.telesrv.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telesrv.plugin.MyPlugin;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final String botToken = "7991087130:AAFAHiiAJyWdqLxGEDel_Fw-OFotQ_gns18"; // Replace with actual bot token
    private final String chatId = "-1002399038601"; // Replace with actual chat ID
    private final MyPlugin mainPlugin;
    private final DiscordBot discordBot; // Add reference to Discord bot

    public MyTelegramBot(MyPlugin plugin, DiscordBot discordBot) {
        this.mainPlugin = plugin;
        this.discordBot = discordBot;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String sender = update.getMessage().getFrom().getFirstName();

            String formattedMessage = "[Telegram] " + sender + ": " + messageText;
            
            // Send message to Minecraft
            mainPlugin.sendMessageToMinecraft(formattedMessage);

            // Send message to Discord
            discordBot.sendMessageToDiscord(formattedMessage);
        }
    }

    public void sendMessageToTelegram(String messageContent) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageContent);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SCP secret laboratory"; // Replace with actual bot username
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

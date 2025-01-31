package com.telesrv.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telesrv.plugin.MyPlugin;
import com.telesrv.utils.ConfigLoader;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final String botToken = ConfigLoader.get("telegram.token");
    private final String chatId = ConfigLoader.get("telegram.chatId");
    private final MyPlugin mainPlugin;
    private DiscordBot discordBot; // ❌ Removed 'final' so we can set it later

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

            // ✅ Check if Discord bot is set before sending message
            if (discordBot != null) {
                discordBot.sendMessageToDiscord(formattedMessage);
            } else {
                System.out.println("⚠ Warning: Discord bot is not initialized yet!");
            }

            
        }
        if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
        for (User member : update.getMessage().getNewChatMembers()) {
            String welcomeMessage = member.getFirstName() + " has joined the Telegram group!";
            
            // Send message to Minecraft and Discord
            mainPlugin.sendMessageToMinecraft("[Minecraft] " + welcomeMessage);
            discordBot.sendMessageToDiscord("[Discord Console] " + welcomeMessage);
        }
    }

    // Detect when members leave the chat
    if (update.hasMessage() && update.getMessage().getLeftChatMember() != null) {
        String leaveMessage = update.getMessage().getLeftChatMember().getFirstName() + " has left the Telegram group!";
        
        // Send message to Minecraft and Discord
        mainPlugin.sendMessageToMinecraft("[Minecraft] " + leaveMessage);
        // Send to Discord chat (log)
        discordBot.sendMessageToDiscord("[Discord Console] " + leaveMessage);
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

    // ✅ Setter to properly set Discord bot after initialization
    public void setDiscordBot(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public String getBotUsername() {
        return "Scp"; // Replace with actual bot username
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    
}

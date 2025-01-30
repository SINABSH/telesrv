package com.telesrv.bot;


import static org.bukkit.Bukkit.getLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MinecraftChatListener implements Listener {

    private final MyTelegramBot telegramBot;

    // Constructor that passes the Telegram bot instance
    public MinecraftChatListener(MyTelegramBot bot) {
        this.telegramBot = bot;
    }

    // This method listens for Minecraft chat messages
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        

        // Send the Minecraft chat message to the Telegram group
        sendToTelegram(playerName, message);
    }

    // Method to send the message to the Telegram group
    private void sendToTelegram(String playerName, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("@yourTelegramGroupChatId"); // Replace with your group chat ID
        sendMessage.setText(playerName + ": " + message);
        

        try {
            telegramBot.execute(sendMessage); // Send message to Telegram group
        } catch (TelegramApiException e) {
            getLogger().info("is it in it?");
        }
    }
}

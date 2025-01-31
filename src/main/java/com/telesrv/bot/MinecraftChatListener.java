package com.telesrv.bot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MinecraftChatListener implements Listener {

    private final MyTelegramBot telegramBot;

    
    public MinecraftChatListener(MyTelegramBot bot) {
        this.telegramBot = bot;
    }

    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        

        
        sendToTelegram(playerName, message);
    }

    
    private void sendToTelegram(String playerName, String message) {

        long chatId = -1002399038601L;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Minecraft: " + playerName + ": " + message);
        

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

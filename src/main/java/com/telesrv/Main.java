package com.telesrv;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.telesrv.bot.MinecraftChatListener;
import com.telesrv.bot.MyTelegramBot;

public class Main extends JavaPlugin {

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void onEnable() {
        getLogger().info("Plugin enabled!");

        // Initialize Telegram Bot
        MyTelegramBot telegramBot = new MyTelegramBot();
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException ex) {
        }
        try {
            if (botsApi != null) {
                botsApi.registerBot(telegramBot);
                getLogger().info("Telegram bot started successfully!");
            } else {
                getLogger().severe("Failed to initialize TelegramBotsApi.");
            }
        } catch (TelegramApiException e) {
            getLogger().log(Level.SEVERE, "Failed to start Telegram bot: {0}", e.getMessage());
            e.printStackTrace();
        }

        // Register Minecraft Chat Listener
        getServer().getPluginManager().registerEvents(new MinecraftChatListener(telegramBot), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }
}

package com.telesrv.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.telesrv.bot.MyTelegramBot;



public class MyPlugin extends JavaPlugin {
    private MyTelegramBot myTelegramBot;  // Telegram bot instance

   
    @Override
public void onEnable() {
    getLogger().info("Plugin is starting!");

    // Initialize your bot instance
    myTelegramBot = new MyTelegramBot();

    // Use TelegramBotsApi to register the bot
    TelegramBotsApi botsApi;
    try {
        botsApi = new TelegramBotsApi(DefaultBotSession.class); // Create a bot session
        botsApi.registerBot(myTelegramBot);  // Register your bot with the API
        getLogger().info("Telegram bot started successfully!");
    } catch (TelegramApiException e) {
        // Handle the exception gracefully and log the error
        getLogger().severe("Failed to start Telegram bot: " + e.getMessage());
        e.printStackTrace();
    }
}
    @Override
    public void onDisable() {
        getLogger().info("Plugin is stopping!");

        // Perform any cleanup if necessary
        // (If your bot has long-running tasks or connections, you can stop them here)
        myTelegramBot = null;
    }
}

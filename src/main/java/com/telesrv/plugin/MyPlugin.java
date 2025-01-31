package com.telesrv.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.telesrv.bot.DiscordBot;
import com.telesrv.bot.MinecraftChatListener;
import com.telesrv.bot.MyTelegramBot;

public class MyPlugin extends JavaPlugin implements Listener {
    private MyTelegramBot myTelegramBot;
    private DiscordBot discordBot;

    @Override
    public void onEnable() {
        getLogger().info("Telesrv Plugin is starting!");

        // Initialize Telegram bot
        myTelegramBot = new MyTelegramBot(this, null); // Temporarily set DiscordBot as null
        
        // Initialize Discord bot
        discordBot = new DiscordBot(this, myTelegramBot);
        discordBot.startBot();

        // Now set TelegramBot's Discord reference
        myTelegramBot = new MyTelegramBot(this, discordBot);

        // Register Minecraft chat listener
        getServer().getPluginManager().registerEvents(new MinecraftChatListener(myTelegramBot), this);
        getServer().getPluginManager().registerEvents(this, this);

        // Start Telegram bot
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myTelegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Telesrv Plugin is stopping!");
        myTelegramBot = null;
        discordBot = null;
    }

    public void sendMessageToMinecraft(String content) {
        getServer().broadcastMessage(content);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getPlayer().getName() + ": " + event.getMessage();

        // Send message to Telegram
        myTelegramBot.sendMessageToTelegram("[Minecraft] " + message);

        // Send message to Discord
        discordBot.sendMessageToDiscord("[Minecraft] " + message);
    }
}

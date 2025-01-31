package com.telesrv.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.telesrv.bot.DiscordBot;
import com.telesrv.bot.MinecraftChatListener;
import com.telesrv.bot.MyTelegramBot;
import com.telesrv.utils.ConfigLoader;

public class MyPlugin extends JavaPlugin implements Listener {
    private MyTelegramBot myTelegramBot;
    private DiscordBot discordBot;

    // Discord bot credentials
    private final String discordToken = ConfigLoader.get("discord.token");
    private final String discordChannelId = ConfigLoader.get("discord.channelId");


    @Override
    public void onEnable() {
        getLogger().info("✅ Telesrv Plugin is starting!");

        // Initialize Telegram bot (but don't set Discord bot yet)
        myTelegramBot = new MyTelegramBot(this, null);

        // Initialize Discord bot with token & channel ID
        String discordToken = ConfigLoader.get("DISCORD_TOKEN");
        String discordChannelId = ConfigLoader.get("DISCORD_CHANNEL_ID");

        discordBot.startBot();

        // Now update Telegram bot with Discord reference
        myTelegramBot.setDiscordBot(discordBot);

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
        getLogger().info("❌ Telesrv Plugin is stopping!");
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
        if (myTelegramBot != null) {
            myTelegramBot.sendMessageToTelegram("[Minecraft] " + message);
        }

        // Send message to Discord
        if (discordBot != null) {
            discordBot.sendMessageToDiscord("[Minecraft] " + message);
        }
    }

    @EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    String message = event.getPlayer().getName() + " has joined the server!";
    
     // Send to Telegram (log channel)
    myTelegramBot.sendMessageToTelegram("[Telegram Console] " + message);
    
    // Send to Discord (log channel)
    discordBot.sendMessageToDiscord("[Discord Console] " + message);
}

@EventHandler
public void onPlayerQuit(PlayerQuitEvent event) {
    String message = event.getPlayer().getName() + " has left the server!";
    // Send to Telegram (log channel)
    myTelegramBot.sendMessageToTelegram("[Telegram Console] " + message);
    
    // Send to Discord (log channel)
    discordBot.sendMessageToDiscord("[Discord Console] " + message);
}

}

package com.telesrv.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.telesrv.bot.MinecraftChatListener;
import com.telesrv.bot.MyTelegramBot;
import com.telesrv.bot.DiscordBot;

public class MyPlugin extends JavaPlugin {
    private MyTelegramBot myTelegramBot;
    private DiscordBot discordBot;

    @Override
    public void onEnable() {
        getLogger().info("Plugin is starting!");

        
        myTelegramBot = new MyTelegramBot(this);
        discordBot = new DiscordBot(this);

        getServer().getPluginManager().registerEvents(new MinecraftChatListener(myTelegramBot), this);
        
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myTelegramBot);
            
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin is stopping!");
        myTelegramBot = null;
    }

    public void sendMessageToMinecraft(String content) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

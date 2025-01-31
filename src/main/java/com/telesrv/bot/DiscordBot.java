package com.telesrv.bot;

import javax.security.auth.login.LoginException;

import com.telesrv.plugin.MyPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {
    private final String discordToken;
    private final String discordChannelId;
    private final MyPlugin mainPlugin;
    private final MyTelegramBot telegramBot;
    private JDA jda;

    public DiscordBot(MyPlugin plugin, MyTelegramBot telegramBot, String discordToken, String discordChannelId) {
        this.mainPlugin = plugin;
        this.telegramBot = telegramBot;
        this.discordToken = discordToken;
        this.discordChannelId = discordChannelId;
    }

    public void startBot() {
        try {
            jda = JDABuilder.createDefault(discordToken)
                    .addEventListeners(this)
                    .build();
            
            // Wait for JDA to be ready before using it
            jda.awaitReady();
            
            System.out.println("✅ Discord bot started successfully!");
        } catch (LoginException | InterruptedException e) {
            System.out.println("❌ Error initializing Discord bot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // Ignore bot messages

        String content = "[Discord] " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay();

        // Send to Minecraft
        mainPlugin.sendMessageToMinecraft(content);

        // Send to Telegram (if Telegram bot is initialized)
        if (telegramBot != null) {
            telegramBot.sendMessageToTelegram(content);
        }
    }

    public void sendMessageToDiscord(String messageContent) {
        if (jda == null) {
            System.out.println("❌ JDA instance is not initialized!");
            return;
        }

        TextChannel channel = jda.getTextChannelById(discordChannelId);
        if (channel != null) {
            channel.sendMessage(messageContent).queue();
        } else {
            System.out.println("❌ Discord channel not found!");
        }
    }
    
}

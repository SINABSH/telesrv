package com.telesrv.bot;

import javax.security.auth.login.LoginException;

import com.telesrv.plugin.MyPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {
    private final String discordToken = "MTMzNDg2OTg3NTMyMzA0ODAyOA.GRy5Lq.81oSIudZV9iL2zhNfMn4_g6wpKn5pQpW7yhRCQ";
    private final String discordChannelId = "1280079130830049333";
    private final MyPlugin mainPlugin;
    private final MyTelegramBot telegramBot; // Reference to Telegram bot
    private JDA jda;

    public DiscordBot(MyPlugin plugin, MyTelegramBot telegramBot) {
        this.mainPlugin = plugin;
        this.telegramBot = telegramBot;
    }

    public void startBot() {
        try {
            jda = JDABuilder.createDefault(discordToken)
                    .addEventListeners(this)
                    .build();
            System.out.println("Discord bot started successfully!");
        } catch (LoginException e) {
            System.out.println("Error initializing Discord bot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // Ignore bot messages

        String content = "[Discord] " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay();
        
        // Send to Minecraft
        mainPlugin.sendMessageToMinecraft(content);

        // Send to Telegram
        telegramBot.sendMessageToTelegram(content);
    }

    public void sendMessageToDiscord(String messageContent) {
        if (jda == null) {
            System.out.println("JDA instance is not initialized!");
            return;
        }

        TextChannel channel = jda.getTextChannelById(discordChannelId);
        if (channel != null) {
            channel.sendMessage(messageContent).queue();
        } else {
            System.out.println("Discord channel not found!");
        }
    }
}

package com.telesrv.bot;

import javax.security.auth.login.LoginException;

import com.telesrv.plugin.MyPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {
    private String discordToken = "MTMzNDg2OTg3NTMyMzA0ODAyOA.GRy5Lq.81oSIudZV9iL2zhNfMn4_g6wpKn5pQpW7yhRCQ";  // Make sure to load this securely (not hardcoded)
    private MyPlugin mainPlugin;  // Reference to your actual plugin class
    private JDA jda;  // Store the JDA instance

    public DiscordBot(MyPlugin plugin) {
        this.mainPlugin = plugin;
    }

    // Method to initialize the bot
    public void startBot() throws LoginException {
        jda = JDABuilder.createDefault(discordToken)  // Store the JDA instance
                .addEventListeners(this)  // Add the listener (this class) to handle events
                .build();  // Build and start the bot
                System.out.println("JDA Bot Initialized Successfully");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore messages from bots
        if (event.getAuthor().isBot()) return;

        // Get the content of the message from Discord
        String content = event.getMessage().getContentDisplay();
        
        // Forward the message from Discord to Minecraft
        mainPlugin.sendMessageToMinecraft(content);  // Call your Minecraft method
    }

    // Method to send a message to Discord from Minecraft
    public void sendMessageToDiscord(String messageContent, String channelId) {
        // Ensure jda is initialized before trying to send a message
        if (jda == null) {
            System.out.println("JDA instance is not initialized!");
            return;
        }

        // Fetch the TextChannel using the JDA instance
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(messageContent).queue();  // Send the message asynchronously
        } else {
            System.out.println("Channel not found with ID: " + channelId);
        }
    }
}

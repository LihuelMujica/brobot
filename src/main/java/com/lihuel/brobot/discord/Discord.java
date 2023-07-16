package com.lihuel.brobot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Discord {

    private final String BOT_TOKEN = System.getenv("DISCORD_TOKEN");

    @Bean
    public JDA jda() {
        JDA jda = JDABuilder.createDefault(BOT_TOKEN).build();
        return jda;
    }

}

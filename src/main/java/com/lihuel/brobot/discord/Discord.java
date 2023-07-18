package com.lihuel.brobot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Discord {

    @Value("${discord.token}")
    private String BOT_TOKEN;

    @Bean
    public JDA jda() {
        JDA jda = JDABuilder.createDefault(BOT_TOKEN).build();
        return jda;
    }

}

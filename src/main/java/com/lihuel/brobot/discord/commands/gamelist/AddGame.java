package com.lihuel.brobot.discord.commands.gamelist;

import com.lihuel.brobot.discord.commands.Command;
import com.lihuel.brobot.service.GameService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddGame implements Command {

    private final GameService gameService;

    @Autowired
    public AddGame(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "agregar-juego";
    }

    @Override
    public String getDescription() {
        return "Agregar un juego a la base de datos de brobot";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "url", "Ingresa la URL de steam del juego")
                        .setRequired(true),
                new OptionData(OptionType.BOOLEAN, "pirateado", "Indica si el juego tiene multiplayer pirata")
                        .setRequired(false)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String url = event.getOption("url").getAsString();
        Boolean hasPiratedMultiplayer = event.getOption("pirateado") != null && event.getOption("pirateado").getAsBoolean();
        String pirateMessage = hasPiratedMultiplayer ? "El juego tiene multiplayer pirata" : " el juego no tiene multiplayer pirata";
        System.out.println(hasPiratedMultiplayer);
        try {
            event.reply("Agregando juego a la base de datos: " + gameService.save(url, hasPiratedMultiplayer).getName() + pirateMessage).queue();
        } catch (Exception e) {
            event.reply("Error al agregar el juego").queue();
        }
    }
}

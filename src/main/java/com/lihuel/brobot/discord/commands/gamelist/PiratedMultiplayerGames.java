package com.lihuel.brobot.discord.commands.gamelist;

import com.lihuel.brobot.discord.commands.Command;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.service.GameService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PiratedMultiplayerGames implements Command {
    private final GameService gameService;

    @Autowired
    public PiratedMultiplayerGames(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "juegos-piratas";
    }

    @Override
    public String getDescription() {
        return "Obtener lista de juegos piratas para jugar en multijugador";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        List<String> games = null;
        try {
            games = gameService.findPiratedMultiplayerGames().stream().map(game -> game.getName()).collect(Collectors.toList());
        } catch (Exception e) {
            event.getHook().sendMessage("Error al obtener la lista de juegos piratas").queue();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de juegos con multiplayer pirateable: \n");
        for (String game : games) {
            sb.append("- ").append(game).append("\n");
        }
        event.getHook().sendMessage(sb.toString()).queue();
    }
}

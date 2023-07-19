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

@Component
public class MyGames implements Command {
    private final GameService gameService;

    @Autowired
    public MyGames(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "mis-juegos";
    }

    @Override
    public String getDescription() {
        return "Te permite ver la lista de juegos de la base de datos de brobot que posees";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String userId = event.getUser().getId();
        List<Game> games;
        try{
            games = gameService.findGamesByDiscordId(userId);
            StringBuilder sb = new StringBuilder();
            sb.append("Tu lista de juegos: \n");
            for (Game game : games) {
                sb.append("- ").append(game.getName()).append("\n");
            }
            event.getHook().sendMessage(sb.toString()).queue();
        } catch (SteamApiException e) {
            event.getHook().sendMessage("No se pudo encontrar tu cuenta de steam, prueba con el comando /vincular-steam").queue();
        }
    }
}

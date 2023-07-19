package com.lihuel.brobot.discord.commands.gamelist;

import com.lihuel.brobot.discord.commands.Command;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.service.GameService;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonGames implements Command {

    private final GameService gameService;

    @Autowired
    public CommonGames(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "juegos-comunes";
    }

    @Override
    public String getDescription() {
        return "Obtener lista de juegos comunes entre tus amigos";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.MENTIONABLE, "usuario1", "usuario1")
                        .setRequired(true),
                new OptionData(OptionType.MENTIONABLE, "usuario2", "usuario2")
                        .setRequired(true),
                new OptionData(OptionType.MENTIONABLE, "usuario3", "usuario3")
                        .setRequired(false),
                new OptionData(OptionType.MENTIONABLE, "usuario4", "usuario4")
                        .setRequired(false),
                new OptionData(OptionType.MENTIONABLE, "usuario5", "usuario5")
                        .setRequired(false),
                new OptionData(OptionType.MENTIONABLE, "usuario6", "usuario6")
                        .setRequired(false),
                new OptionData(OptionType.MENTIONABLE, "usuario7", "usuario7")
                        .setRequired(false),
                new OptionData(OptionType.MENTIONABLE, "usuario8", "usuario8")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        List<IMentionable> users =  event.getOptionsByType(OptionType.MENTIONABLE).stream().map(OptionMapping::getAsMentionable).toList();
        List<Game> games;
        try{
            games = gameService.findGamesInCommon(users.stream().map(IMentionable::getId).toList());
            System.out.println(games);
            StringBuilder sb = new StringBuilder();
            sb.append("Juegos comunes entre ").append(users.stream().map(IMentionable::getAsMention).collect(Collectors.joining(", "))).append(": \n");
            for (Game game : games) {
                sb.append("- ").append(game.getName()).append("\n");
            }
            System.out.println(sb.toString());
            event.getHook().sendMessage(sb.toString()).queue();
        } catch (SteamApiException e) {
            event.getHook().sendMessage("No se pudo encontrar tu cuenta de steam, prueba con el comando /vincular-steam").queue();
        }
    }
}

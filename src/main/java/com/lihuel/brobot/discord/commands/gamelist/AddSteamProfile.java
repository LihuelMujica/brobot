package com.lihuel.brobot.discord.commands.gamelist;

import com.lihuel.brobot.discord.commands.Command;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.service.UserService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddSteamProfile implements Command {

    private final UserService gamerUserService;

    @Autowired
    public AddSteamProfile(UserService gamerUserService) {
        this.gamerUserService = gamerUserService;
    }

    @Override
    public String getName() {
        return "vincular-steam";
    }

    @Override
    public String getDescription() {
        return "Con este comando puedes vincular tu perfil de steam a tu cuenta de brobot";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.MENTIONABLE, "usuario", "Usuario de discord a vincular")
                        .setRequired(true),
                new OptionData(OptionType.STRING, "steamId", "ID que quieres vincular")
                        .setRequired(true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String steamId = event.getOption("steamId").getAsString();
        User user = event.getOption("usuario").getAsUser();

        try {
            com.lihuel.brobot.model.User appuser = gamerUserService.saveUserByDiscordIdAndSteamId(user.getId(), steamId);
            event.reply("Se ha vinculado el usuario " + user.getAsMention() + " con la cuenta de steam " + appuser.getSteamProfileUrl()).queue();
            return;
        } catch (SteamApiException e) {
            event.reply("No has ingresado una id de steam válida").queue();
            return;
        }
    }
}

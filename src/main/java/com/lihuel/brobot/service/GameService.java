package com.lihuel.brobot.service;

import com.lihuel.brobot.dto.CommonGames;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;

import java.util.List;

public interface GameService {

    public Game save(String steamUrl, Boolean hasPiratedMultiplayer) throws SteamApiException;
    public List<Game> findGamesByDiscordId(String discordId) throws SteamApiException;

    public List<Game> findGamesInCommon(List<String> discordIds) throws SteamApiException;
    public List<Game> findPiratedMultiplayerGames();

    public CommonGames findCommonGames(List<String> discordIds) throws SteamApiException;

}

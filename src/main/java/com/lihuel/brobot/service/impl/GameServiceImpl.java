package com.lihuel.brobot.service.impl;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.repository.GameRepository;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.service.GameService;
import com.lihuel.brobot.utils.SteamURLUtil;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final SteamApi steamApi;

    private final GameRepository gameRepository;

    public GameServiceImpl(SteamApi steamApi, GameRepository gameRepository) {
        this.steamApi = steamApi;
        this.gameRepository = gameRepository;
    }


    @Override
    public Game save(String steamUrl) throws SteamApiException {
        if (!SteamURLUtil.isValidSteamURL(steamUrl)) throw new IllegalArgumentException("La url no es una url de steam válida");
        String steamId = SteamURLUtil.extractAppIDFromSteamURL(steamUrl);
        if (steamId.isEmpty()) throw new IllegalArgumentException("No has ingresado un juego válido");
        SteamGameDTO.GameDetails gameDetails = steamApi.getAppDetails(steamId).getData();
        Game game = new Game();
        game.setSteamId(steamId);
        game.setName(gameDetails.getName());
        game.setSteamUrl(steamUrl);

        return gameRepository.save(game);
    }
}

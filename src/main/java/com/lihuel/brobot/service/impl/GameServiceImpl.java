package com.lihuel.brobot.service.impl;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.dto.SteamOwnedGameDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.model.User;
import com.lihuel.brobot.repository.GameRepository;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.repository.UserRepository;
import com.lihuel.brobot.service.GameService;
import com.lihuel.brobot.utils.SteamURLUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final SteamApi steamApi;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameServiceImpl(SteamApi steamApi, GameRepository gameRepository, UserRepository userRepository) {
        this.steamApi = steamApi;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Game save(String steamUrl, Boolean hasPiratedMultiplayer) throws SteamApiException {
        if (!SteamURLUtil.isValidSteamURL(steamUrl)) throw new IllegalArgumentException("La url no es una url de steam válida");
        String steamId = SteamURLUtil.extractAppIDFromSteamURL(steamUrl);
        if (steamId.isEmpty()) throw new IllegalArgumentException("No has ingresado un juego válido");
        SteamGameDTO.GameDetails gameDetails = steamApi.getAppDetails(steamId).getData();
        Game game = new Game();
        game.setSteamId(steamId);
        game.setName(gameDetails.getName());
        game.setSteamUrl(steamUrl);
        game.setHasPiratedMultiplayer(hasPiratedMultiplayer);

        return gameRepository.save(game);
    }

    @Override
    public List<Game> findGamesByDiscordId(String discordId) throws SteamApiException {
        User user = userRepository.findById(discordId).orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario"));
        Set<SteamOwnedGameDTO.Game> ownedGamesDTO = steamApi.getOwnedGames(user.getSteamUserId());
        return gameRepository.findAllById(ownedGamesDTO.stream().map(SteamOwnedGameDTO.Game::getAppId).toList());
    }

    @Override
    public List<Game> findGamesInCommon(List<String> discordIds) throws SteamApiException {
        List<User> users = userRepository.findAllById(discordIds);
        Set<String> commonGamesIds = new HashSet<>();
        HashMap<String, Set<String>> ownedGamesIds = new HashMap<>();

        for (User user : users) {
            ownedGamesIds.put(user.getDiscordId(), steamApi.getOwnedGames(user.getSteamUserId()).stream().map(SteamOwnedGameDTO.Game::getAppId).collect(Collectors.toSet()));
        }

        for (User user : users) {
            if (commonGamesIds.size() == 0) {
                commonGamesIds.addAll(ownedGamesIds.get(user.getDiscordId()));
            } else {
                commonGamesIds.retainAll(ownedGamesIds.get(user.getDiscordId()));
            }
        }

        return gameRepository.findAllById(commonGamesIds);

    }



}

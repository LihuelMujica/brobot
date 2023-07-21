package com.lihuel.brobot.service;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.dto.SteamOwnedGameDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.model.User;
import com.lihuel.brobot.repository.GameRepository;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.repository.UserRepository;
import com.lihuel.brobot.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;


class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private SteamApi steamApi;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private List<Game> games;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        games = List.of(
                new Game("123456", false, "Test Game1", "https://store.steampowered.com/app/123456/Test_Game1/"),
                new Game("1234567", false, "Test Game2", "https://store.steampowered.com/app/1234567/Test_Game2/"),
                new Game("6543210", false, "Test Game3", "https://store.steampowered.com/app/6543210/Test_Game3/"),
                new Game("7654321", false, "Test Game4", "https://store.steampowered.com/app/7654321/Test_Game4/"),
                new Game("76543210", false, "Test Game5", "https://store.steampowered.com/app/76543210/Test_Game5/"),
                new Game("765432109", false, "Test Game6", "https://store.steampowered.com/app/765432109/Test_Game6/"),
                new Game("7654321098", false, "Test Game7", "https://store.steampowered.com/app/7654321098/Test_Game7/"),
                new Game("76543210987", false, "Test Game8", "https://store.steampowered.com/app/76543210987/Test_Game8/")
        );
    }

    @Test
    void save() throws SteamApiException {
        SteamGameDTO.GameDetails gameDetails = new SteamGameDTO.GameDetails();
        gameDetails.setName("Test Game");
        SteamGameDTO steamGameDTO = new SteamGameDTO();
        steamGameDTO.setData(gameDetails);
        steamGameDTO.setSuccess(true);
        when(steamApi.getAppDetails("123456")).thenReturn(steamGameDTO);
        when(gameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Game game = gameService.save("https://store.steampowered.com/app/123456/Test_Game/", false);

        verify(gameRepository, times(1)).save(any());
        assertEquals("123456", game.getSteamId());
        assertEquals("Test Game", game.getName());
        assertEquals("https://store.steampowered.com/app/123456/Test_Game/", game.getSteamUrl());
    }

    @Test
    void saveNoSteamUrl() {
        assertThrows(IllegalArgumentException.class, () -> gameService.save("https://store.facebook.com", false));
    }

    @Test
    void saveInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> gameService.save("https://store.steampowered.com/app/", false));
    }

    @Test
    void saveWithSteamApiException() throws SteamApiException {
        when(steamApi.getAppDetails("123456")).thenThrow(new SteamApiException("Error"));
        assertThrows(SteamApiException.class, () -> gameService.save("https://store.steampowered.com/app/123456/Test_Game/", false));
    }

    @Test
    void shouldFindGamesByDiscordId() throws SteamApiException {
        SteamOwnedGameDTO.Game game1 = new SteamOwnedGameDTO.Game();
        game1.setAppId("123456");
        SteamOwnedGameDTO.Game game2 = new SteamOwnedGameDTO.Game();
        game2.setAppId("1234567");
        SteamOwnedGameDTO.Game game3 = new SteamOwnedGameDTO.Game();
        game3.setAppId("6543210");
        User user = new User();
        user.setDiscordId("1234");
        user.setSteamUserId("1234");
        user.setSteamProfileUrl("https://steamcommunity.com/id/1234");

        when(steamApi.getOwnedGames("1234")).thenReturn(Set.of(game1, game2, game3));
        when(gameRepository.findAllById(any())).thenAnswer(i -> {
            List<String> gameIds = (List<String>) i.getArguments()[0];
            return games.stream().filter(game -> gameIds.contains(game.getSteamId())).collect(Collectors.toList());
        });        when(userRepository.findById("1234")).thenReturn(Optional.of(user));

        List<Game> gamesByDiscordId = gameService.findGamesByDiscordId("1234");

        verify(steamApi, times(1)).getOwnedGames("1234");
        verify(gameRepository, times(1)).findAllById(any());
        assertEquals(3, gamesByDiscordId.size());
        assertEquals("123456", gamesByDiscordId.get(0).getSteamId());
        assertEquals("Test Game1", gamesByDiscordId.get(0).getName());
        assertEquals("https://store.steampowered.com/app/123456/Test_Game1/", gamesByDiscordId.get(0).getSteamUrl());
        assertEquals("1234567", gamesByDiscordId.get(1).getSteamId());
        assertEquals("Test Game2", gamesByDiscordId.get(1).getName());
        assertEquals("https://store.steampowered.com/app/1234567/Test_Game2/", gamesByDiscordId.get(1).getSteamUrl());
        assertEquals("6543210", gamesByDiscordId.get(2).getSteamId());
        assertEquals("Test Game3", gamesByDiscordId.get(2).getName());
        assertEquals("https://store.steampowered.com/app/6543210/Test_Game3/", gamesByDiscordId.get(2).getSteamUrl());

    }

    @Test
    void shouldFindGamesInCommon() throws SteamApiException {
        User user1 = new User("1234", "1234", "https://steamcommunity.com/id/1234");
        User user2 = new User("5678", "5678", "https://steamcommunity.com/id/5678");
        User user3 = new User("9012", "9012", "https://steamcommunity.com/id/9012");
        when(steamApi.getOwnedGames(user1.getDiscordId())).thenReturn(
                Set.of(
                        new SteamOwnedGameDTO.Game(games.get(0).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(1).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(2).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(3).getSteamId())
                )
        );

        when(steamApi.getOwnedGames(user2.getSteamUserId())).thenReturn(
                Set.of(
                        new SteamOwnedGameDTO.Game(games.get(0).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(1).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(2).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(3).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(4).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(5).getSteamId())
                )
        );

        when(steamApi.getOwnedGames(user3.getSteamUserId())).thenReturn(
                Set.of(
                        new SteamOwnedGameDTO.Game(games.get(0).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(1).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(2).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(5).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(6).getSteamId()),
                        new SteamOwnedGameDTO.Game(games.get(7).getSteamId())
                )
        );

        when(userRepository.findAllById(any())).thenReturn(List.of(user1, user2, user3));
        when(gameRepository.findAllById(any())).thenAnswer(i -> {
            Set<String> gameIds = (Set<String>) i.getArguments()[0];
            return games.stream().filter(game -> gameIds.contains(game.getSteamId())).collect(Collectors.toList());
        });

        List<Game> gamesInCommon = gameService.findGamesInCommon(List.of(user1.getDiscordId(), user2.getDiscordId(), user3.getDiscordId()));

        verify(steamApi, times(1)).getOwnedGames(user1.getSteamUserId());
        verify(steamApi, times(1)).getOwnedGames(user2.getSteamUserId());
        verify(steamApi, times(1)).getOwnedGames(user3.getSteamUserId());
        verify(gameRepository, times(1)).findAllById(any());
        assertEquals(3, gamesInCommon.size());
        assertEquals("123456", gamesInCommon.get(0).getSteamId());
        assertEquals("Test Game1", gamesInCommon.get(0).getName());
        assertEquals("https://store.steampowered.com/app/123456/Test_Game1/", gamesInCommon.get(0).getSteamUrl());
        assertEquals("1234567", gamesInCommon.get(1).getSteamId());
        assertEquals("Test Game2", gamesInCommon.get(1).getName());
        assertEquals("https://store.steampowered.com/app/1234567/Test_Game2/", gamesInCommon.get(1).getSteamUrl());
        assertEquals("6543210", gamesInCommon.get(2).getSteamId());
        assertEquals("Test Game3", gamesInCommon.get(2).getName());
        assertEquals("https://store.steampowered.com/app/6543210/Test_Game3/", gamesInCommon.get(2).getSteamUrl());


    }

}
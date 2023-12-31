package com.lihuel.brobot.repository;

import com.lihuel.brobot.model.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        List<Game> games = List.of(
            new Game(),
            new Game(),
            new Game(),
            new Game(),
            new Game(),
            new Game()
        );

        games.get(0).setName("test1");
        games.get(0).setHasPiratedMultiplayer(true);
        games.get(0).setSteamId("test1");
        games.get(0).setSteamUrl("https://store.steampowered.com/app/1");
        games.get(1).setName("test2");
        games.get(1).setHasPiratedMultiplayer(false);
        games.get(1).setSteamId("test2");
        games.get(1).setSteamUrl("https://store.steampowered.com/app/2");
        games.get(2).setName("test3");
        games.get(2).setHasPiratedMultiplayer(true);
        games.get(2).setSteamId("test3");
        games.get(2).setSteamUrl("https://store.steampowered.com/app/3");
        games.get(3).setName("test4");
        games.get(3).setHasPiratedMultiplayer(false);
        games.get(3).setHasSteamPlayTogether(true);
        games.get(3).setSteamId("test4");
        games.get(3).setSteamUrl("https://store.steampowered.com/app/4");
        games.get(4).setName("test5");
        games.get(4).setHasPiratedMultiplayer(true);
        games.get(4).setHasSteamPlayTogether(true);
        games.get(4).setSteamId("test5");
        games.get(4).setSteamUrl("https://store.steampowered.com/app/5");
        games.get(5).setName("test6");
        games.get(5).setHasPiratedMultiplayer(false);
        games.get(5).setHasSteamPlayTogether(true);
        games.get(5).setSteamId("test6");
        games.get(5).setSteamUrl("https://store.steampowered.com/app/6");

        gameRepository.saveAll(games);

    }

    @AfterEach
    void tearDown() {
        gameRepository.deleteAll(gameRepository.findAllById(List.of("test1", "test2", "test3", "test4", "test5", "test6")));
    }

    @Test void crud() {
        Game game = new Game();
        game.setName("test");
        game.setHasPiratedMultiplayer(true);
        game.setSteamId("test123");
        game.setSteamUrl("https://store.steampowered.com/app/123");

        gameRepository.save(game);

        Game game2 = gameRepository.findById(game.getSteamId()).orElse(null);

        assertNotNull(game2);
        assertEquals(game.getName(), game2.getName());
        assertEquals(game.getHasPiratedMultiplayer(), game2.getHasPiratedMultiplayer());
        assertEquals(game.getSteamId(), game2.getSteamId());
        assertEquals(game.getSteamUrl(), game2.getSteamUrl());

        gameRepository.delete(game);

        Game game3 = gameRepository.findById(game.getSteamId()).orElse(null);
        assertNull(game3);
    }

    @Test
    void findAllBySteamIds() {
        List<Game> games = gameRepository.findAllById(List.of("test1", "test2", "test3"));
        assertEquals(3, games.size());
        assertEquals("test1", games.get(0).getName());
        assertEquals("test2", games.get(1).getName());
        assertEquals("test3", games.get(2).getName());
        assertEquals(true, games.get(0).getHasPiratedMultiplayer());
        assertEquals(false, games.get(1).getHasPiratedMultiplayer());
        assertEquals(true, games.get(2).getHasPiratedMultiplayer());
        assertEquals("https://store.steampowered.com/app/1", games.get(0).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/2", games.get(1).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/3", games.get(2).getSteamUrl());
    }

    @Test
    void findBySteamIdInAndHasSteamPlayTogetherIsTrue() {
        List<Game> games = gameRepository.findBySteamIdInAndHasSteamPlayTogetherIsTrue(List.of("test4", "test5", "test6"));
        assertEquals(3, games.size());
        assertEquals("test4", games.get(0).getName());
        assertEquals("test5", games.get(1).getName());
        assertEquals("test6", games.get(2).getName());
        assertEquals(false, games.get(0).getHasPiratedMultiplayer());
        assertEquals(true, games.get(1).getHasPiratedMultiplayer());
        assertEquals(false, games.get(2).getHasPiratedMultiplayer());
        assertEquals(true, games.get(0).getHasSteamPlayTogether());
        assertEquals(true, games.get(1).getHasSteamPlayTogether());
        assertEquals(true, games.get(2).getHasSteamPlayTogether());
        assertEquals("https://store.steampowered.com/app/4", games.get(0).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/5", games.get(1).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/6", games.get(2).getSteamUrl());
    }

    @Test
    void findByHasPiratedMultiplayerIsTrue() {
        List<Game> games = gameRepository.findByHasPiratedMultiplayerIsTrue();
        games = games.stream().filter(game -> game.getSteamId().contains("test")).toList();

        assertEquals(3, games.size());
        assertEquals("test1", games.get(0).getName());
        assertEquals("test3", games.get(1).getName());
        assertEquals("test5", games.get(2).getName());
        assertEquals(true, games.get(0).getHasPiratedMultiplayer());
        assertEquals(true, games.get(1).getHasPiratedMultiplayer());
        assertEquals(true, games.get(2).getHasPiratedMultiplayer());
        assertEquals("https://store.steampowered.com/app/1", games.get(0).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/3", games.get(1).getSteamUrl());
        assertEquals("https://store.steampowered.com/app/5", games.get(2).getSteamUrl());
    }

}
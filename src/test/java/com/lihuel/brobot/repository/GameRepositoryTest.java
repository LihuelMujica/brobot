package com.lihuel.brobot.repository;

import com.lihuel.brobot.model.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

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


}
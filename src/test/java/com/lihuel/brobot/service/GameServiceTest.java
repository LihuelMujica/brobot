package com.lihuel.brobot.service;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;
import com.lihuel.brobot.repository.GameRepository;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;


class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private SteamApi steamApi;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        Game game = gameService.save("https://store.steampowered.com/app/123456/Test_Game/");

        verify(gameRepository, times(1)).save(any());
        assertEquals("123456", game.getSteamId());
        assertEquals("Test Game", game.getName());
        assertEquals("https://store.steampowered.com/app/123456/Test_Game/", game.getSteamUrl());
    }

    @Test
    void saveNoSteamUrl() {
        assertThrows(IllegalArgumentException.class, () -> gameService.save("https://store.facebook.com"));
    }

    @Test
    void saveInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> gameService.save("https://store.steampowered.com/app/"));
    }

    @Test
    void saveWithSteamApiException() throws SteamApiException {
        when(steamApi.getAppDetails("123456")).thenThrow(new SteamApiException("Error"));
        assertThrows(SteamApiException.class, () -> gameService.save("https://store.steampowered.com/app/123456/Test_Game/"));
    }

}
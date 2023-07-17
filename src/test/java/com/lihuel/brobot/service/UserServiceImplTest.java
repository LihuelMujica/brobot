package com.lihuel.brobot.service;

import com.lihuel.brobot.dto.SteamProfileDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.User;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SteamApi steamApi;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserByDiscordIdAndSteamIdWhenUserDoesNotExist() throws SteamApiException {
        SteamProfileDTO.Player player = new SteamProfileDTO.Player();
        player.setProfileurl("https://steamcommunity.com/id/test/");
        when(steamApi.getSteamProfile("123456")).thenReturn(player);
        when(userRepository.findById("123456")).thenReturn(java.util.Optional.empty());
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.saveUserByDiscordIdAndSteamId("123456", "123456");

        verify(userRepository, times(1)).save(any());
        assertEquals("123456", user.getDiscordId());
        assertEquals("123456", user.getSteamUserId());
        assertEquals("https://steamcommunity.com/id/test/", user.getSteamProfileUrl());
    }

    @Test
    void updateUserByDiscordIdAndSteamId() throws SteamApiException {
        SteamProfileDTO.Player player = new SteamProfileDTO.Player();
        player.setProfileurl("https://steamcommunity.com/id/test/");
        player.setSteamid("123456");
        User user = new User();
        user.setSteamUserId("123456");
        user.setDiscordId("000000");
        user.setSteamProfileUrl("https://steamcommunity.com/id/old/");

        when(steamApi.getSteamProfile("123456")).thenReturn(player);
        when(userRepository.findById("123456")).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User updatedUser = userService.saveUserByDiscordIdAndSteamId("123456", "123456");

        verify(userRepository, times(1)).save(any());
        assertEquals("123456", updatedUser.getDiscordId());
        assertEquals("123456", updatedUser.getSteamUserId());
        assertEquals("https://steamcommunity.com/id/test/", updatedUser.getSteamProfileUrl());

    }

    @Test
    void shouldThrowSteamApiExceptionWhenSteamProfileDoesNotExists() throws SteamApiException {
        when(steamApi.getSteamProfile("123456")).thenThrow(new SteamApiException("Steam profile does not exist"));

        assertThrows(SteamApiException.class, () -> userService.saveUserByDiscordIdAndSteamId("123456", "123456"));
    }



}
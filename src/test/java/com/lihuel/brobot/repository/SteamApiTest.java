package com.lihuel.brobot.repository;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.dto.SteamProfileDTO;
import com.lihuel.brobot.exception.SteamApiException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SteamApiTest {

    private final SteamApi steamApi = new SteamApi();

    @Test
    void getCounterStrikeGlobalOffensiveDetails() throws SteamApiException {
        SteamGameDTO.GameDetails gameDetails = steamApi.getAppDetails("730").getData();
        assertEquals("Counter-Strike: Global Offensive", gameDetails.getName());
    }

    @Test
    void getLeft4Dead2Details() throws SteamApiException {
        SteamGameDTO.GameDetails gameDetails = steamApi.getAppDetails("550").getData();
        assertEquals("Left 4 Dead 2", gameDetails.getName());
    }

    @Test
    void sendInvalidAppId() {
        assertThrows(FeignException.class, () -> steamApi.getAppDetails("0"));
    }

    @Test
    void sendInvalidAppId2() {
        assertThrows(FeignException.class, () -> steamApi.getAppDetails("-1"));
    }

    @Test
    void gettingUnknownApp() {
        assertThrows(SteamApiException.class, () -> steamApi.getAppDetails("999999999999999999"));
    }

    @Test
    void getSteamProfile() throws SteamApiException {
        SteamProfileDTO.Player player = steamApi.getSteamProfile("76561198104013274");
        assertEquals("Lihuel", player.getPersonaname());
        assertEquals("https://steamcommunity.com/id/lihuel/", player.getProfileurl());
    }

    @Test
    void getInvalidProfile() {
        assertThrows(SteamApiException.class, () -> steamApi.getSteamProfile("0"));
    }



}
package com.lihuel.brobot.repository;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.dto.SteamOwnedGameDTO;
import com.lihuel.brobot.dto.SteamProfileDTO;
import com.lihuel.brobot.exception.SteamApiException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SteamApiTest {

    private static SteamApi steamApi;

    @BeforeAll
    static void setup() {
        String steamKey = System.getenv("STEAM_TOKEN");
        steamApi = new SteamApi(steamKey);
    }



    @Test
    void getCounterStrikeGlobalOffensiveDetails() throws SteamApiException {
        SteamGameDTO.GameDetails gameDetails = steamApi.getAppDetails("730").getData();
        assertEquals("Counter-Strike: Global Offensive", gameDetails.getName());
        List<SteamGameDTO.Category> categories = Arrays.asList(gameDetails.getCategories());
        assertTrue(categories.stream().anyMatch(category -> Objects.equals(category.getDescription(), "Multijugador")));
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

    @Test
    void getOwnedGamesByUserIdUserLihuelShouldOwnResidentEvil5() throws SteamApiException {
        Set<SteamOwnedGameDTO.Game> ownedGames = steamApi.getOwnedGames("76561198104013274");
        SteamOwnedGameDTO.Game game = new SteamOwnedGameDTO.Game();
        game.setAppId("21690");
        assertTrue(ownedGames.contains(game));
    }



}
package com.lihuel.brobot.repository;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.dto.SteamOwnedGameDTO;
import com.lihuel.brobot.dto.SteamProfileDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.repository.decoder.steam.SteamDevApiDecoder;
import com.lihuel.brobot.repository.decoder.steam.SteamStoreDecoder;
import feign.Feign;
import feign.FeignException;
import feign.Param;
import feign.RequestLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
public class SteamApi {

    private final String STEAM_KEY;

    public SteamApi(@Value("${steam.token}") String steamKey) {
        STEAM_KEY = steamKey;
    }

    private interface SteamStoreApi {
        @RequestLine("GET /api/appdetails?appids={appid}&l=spanish")
        Map<String, SteamGameDTO> getAppDetails(@Param("appid") String appid);
    }

    private interface SteamDevAPI {
        @RequestLine("GET /ISteamUser/GetPlayerSummaries/v0002/?key={key}&steamids={steamid}&format=json")
        SteamProfileDTO getSteamProfile(@Param("steamid") String steamid, @Param("key") String key);

        @RequestLine("GET /iplayerservice/GetOwnedGames/v0001/?key={key}&steamid={steamid}&format=json&include_appinfo=true")
        SteamOwnedGameDTO getOwnedGames(@Param("steamid") String steamid, @Param("key") String key);


    }


    private final SteamStoreApi steamStoreApi = Feign.builder().decoder(new SteamStoreDecoder()).target(SteamStoreApi.class, "https://store.steampowered.com");

    private final SteamDevAPI steamDevAPI = Feign.builder().decoder(new SteamDevApiDecoder()).target(SteamDevAPI.class, "https://api.steampowered.com");

    public SteamGameDTO getAppDetails(String appid) throws FeignException, SteamApiException {
        Map<String, SteamGameDTO> appdetails =  steamStoreApi.getAppDetails(appid);
        if (appdetails.get(appid).getData() == null) throw new SteamApiException("App not found");
        return appdetails.get(appid);
    }

    public SteamProfileDTO.Player getSteamProfile(String steamid) throws FeignException, SteamApiException {
        SteamProfileDTO steamProfileDTO = steamDevAPI.getSteamProfile(steamid, STEAM_KEY);
        if (steamProfileDTO.getResponse().getPlayers().length == 0) throw new SteamApiException("User not found");
        return steamProfileDTO.getResponse().getPlayers()[0];
    }

    public Set<SteamOwnedGameDTO.Game> getOwnedGames(String steamid) throws FeignException, SteamApiException {
        SteamOwnedGameDTO steamOwnedGameDTO = steamDevAPI.getOwnedGames(steamid, STEAM_KEY);
        if (steamOwnedGameDTO.getResponse() == null) throw new SteamApiException("User not found");
        return steamOwnedGameDTO.getResponse().getGames();
    }
}

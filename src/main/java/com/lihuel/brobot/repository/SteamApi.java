package com.lihuel.brobot.repository;

import com.lihuel.brobot.dto.SteamGameDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.repository.decoder.steam.SteamStoreDecoder;
import feign.Feign;
import feign.FeignException;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public class SteamApi {

    private final String STEAM_KEY = "C4022C2B295FD9C9D7E5D917D632235B";

    private interface SteamStoreApi {
        @RequestLine("GET /api/appdetails?appids={appid}")
        Map<Long, SteamGameDTO> getAppDetails(@Param("appid") long appid);
    }


    private final SteamStoreApi steamStoreApi = Feign.builder().decoder(new SteamStoreDecoder()).target(SteamStoreApi.class, "https://store.steampowered.com");

    public SteamGameDTO getAppDetails(Long appid) throws FeignException, SteamApiException {
        Map<Long, SteamGameDTO> appdetails =  steamStoreApi.getAppDetails(appid);
        if (appdetails.get(appid).getData() == null) throw new SteamApiException("App not found");
        return appdetails.get(appid);
    }
}

package com.lihuel.brobot.service;

import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.Game;

public interface GameService {

    public Game save(String steamUrl) throws SteamApiException;
}

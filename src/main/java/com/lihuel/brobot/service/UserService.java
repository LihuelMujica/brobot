package com.lihuel.brobot.service;

import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.User;

public interface UserService {
    User saveUserByDiscordIdAndSteamId(String discordId, String steamId) throws SteamApiException;
}

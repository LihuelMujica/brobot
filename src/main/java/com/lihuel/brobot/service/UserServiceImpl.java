package com.lihuel.brobot.service;

import com.lihuel.brobot.dto.SteamProfileDTO;
import com.lihuel.brobot.exception.SteamApiException;
import com.lihuel.brobot.model.User;
import com.lihuel.brobot.repository.SteamApi;
import com.lihuel.brobot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SteamApi steamApi;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SteamApi steamApi) {
        this.userRepository = userRepository;
        this.steamApi = steamApi;
    }

    @Override
    public User saveUserByDiscordIdAndSteamId(String discordId, String steamId) throws SteamApiException {
        SteamProfileDTO.Player player = steamApi.getSteamProfile(steamId);
        User user = userRepository.findById(discordId).orElse(new User());
        user.setDiscordId(discordId);
        user.setSteamUserId(steamId);
        user.setSteamProfileUrl(player.getProfileurl());
        return userRepository.save(user);
    }
}

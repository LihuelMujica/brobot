package com.lihuel.brobot.dto;

import com.lihuel.brobot.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonGames {
    private List<Game> commonSteamGames;
    private List<Game> remotePlayTogetherAvailableGames;
    private List<Game> piratedMultiplayerGames;
}

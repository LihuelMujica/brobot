package com.lihuel.brobot.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Set;

@Data
public class SteamOwnedGameDTO {
    @SerializedName("response")
    private Response response;

    @Data
    public static class Response {
        @SerializedName("game_count")
        private int gameCount;

        @SerializedName("games")
        private Set<Game> games;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @ToString
    @AllArgsConstructor
    public static class Game {
        @SerializedName("appid")
        private String appId;

        @SerializedName("playtime_2weeks")
        private int playtime2Weeks;

        @SerializedName("playtime_forever")
        private int playtimeForever;

        public Game(String appId) {
            this.appId = appId;
        }

        @Override
        public int hashCode() {
            return appId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Game && ((Game) obj).appId.equals(appId);
        }

    }
}

package com.lihuel.brobot.dto;

import lombok.Data;

@Data
public class SteamProfileDTO {

    private Response response;

    @Data
    public static class Response {
        private Player[] players;
    }

    @Data
    public static class Player {
        private String steamid;
        private int communityvisibilitystate;
        private int profilestate;
        private String personaname;
        private String profileurl;
        private String avatar;
        private String avatarmedium;
        private String avatarfull;
        private String avatarhash;
        private int lastlogoff;
        private int personastate;
        private String realname;
        private String primaryclanid;
        private int timecreated;
        private int personastateflags;
        private String loccountrycode;
        private String locstatecode;
        private int loccityid;
    }
}

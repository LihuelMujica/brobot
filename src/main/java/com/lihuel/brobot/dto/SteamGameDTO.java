package com.lihuel.brobot.dto;

import lombok.Data;

@Data
public class SteamGameDTO {

    private boolean success;
    private GameDetails data;

    public boolean isSuccess() {
        return success;
    }

    public GameDetails getData() {
        return data;
    }

    @Data
    public class GameDetails {
        private String name;
        private String steam_appid;
        private boolean is_free;
        private String header_image;
        private Screenshot[] screenshots;
        private PCRequirements pc_requirements;

        public String getName() {
            return name;
        }

        public boolean isFree() {
            return is_free;
        }

        public String getHeaderImage() {
            return header_image;
        }

        public Screenshot[] getScreenshots() {
            return screenshots;
        }

        public PCRequirements getPcRequirements() {
            return pc_requirements;
        }
    }

    @Data
    public static class Screenshot {
        private String path_thumbnail;
        private String path_full;

        public String getPathThumbnail() {
            return path_thumbnail;
        }

        public String getPathFull() {
            return path_full;
        }
    }

    @Data
    public static class PCRequirements {
        private String minimum;
        private String recommended;

        public String getMinimum() {
            return minimum;
        }

        public String getRecommended() {
            return recommended;
        }
    }

}

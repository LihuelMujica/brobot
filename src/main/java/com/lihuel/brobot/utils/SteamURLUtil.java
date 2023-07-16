package com.lihuel.brobot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for working with Steam URLs.
 */
public class SteamURLUtil {


    /**
     * Extracts the appID from a Steam URL.
     *
     * @param url the Steam URL
     * @return the appID extracted from the URL, empty string if not found
     */
    public static String extractAppIDFromSteamURL(String url) {
        String appID = "";

        // Regular expression to validate Steam URL and extract appID
        String regex = "https?:\\/\\/store\\.steampowered\\.com\\/app\\/([0-9]+)\\/.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            appID = matcher.group(1);
        }

        return appID;
    }

    /**
     * Checks if a given URL is a valid Steam URL.
     *
     * @param url the URL to be validated
     * @return true if the URL is a valid Steam URL, false otherwise
     */
    public static boolean isValidSteamURL(String url) {
        // Regular expression to validate Steam URL
        String regex = "https?:\\/\\/store\\.steampowered\\.com\\/app\\/([0-9]+)\\/.+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }

}

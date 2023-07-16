package com.lihuel.brobot.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SteamURLUtilTest {


    @Test
    public void testGetSteamIdFromValidUrl() {
        String url = "https://store.steampowered.com/app/730/CounterStrike_Global_Offensive/";
        String expected = "730";
        String actual = SteamURLUtil.extractAppIDFromSteamURL(url);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSteamIdFromInvalidUrl() {
        String url = "https://store.steampowered.com/";
        String expected = "";
        String actual = SteamURLUtil.extractAppIDFromSteamURL(url);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidSteamURLWithValidUrl() {
        String url = "https://store.steampowered.com/app/730/CounterStrike_Global_Offensive/";
        assertTrue(SteamURLUtil.isValidSteamURL(url));
    }

    @Test
    public void testIsValidSteamURLWithInvalidUrl() {
        String url = "https://facebook.com/";
        assertFalse(SteamURLUtil.isValidSteamURL(url));
    }

}
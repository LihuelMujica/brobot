package com.lihuel.brobot.repository.decoder.steam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;

public class SteamDevApiDecoder implements Decoder {

    private final Gson gson;

    public SteamDevApiDecoder() {
        this.gson = new GsonBuilder().create();
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String responseBody = Util.toString(response.body().asReader());
        return gson.fromJson(responseBody, type);
    }

}

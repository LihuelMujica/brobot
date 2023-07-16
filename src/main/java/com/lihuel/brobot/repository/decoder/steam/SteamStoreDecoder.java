package com.lihuel.brobot.repository.decoder.steam;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class SteamStoreDecoder implements Decoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SteamStoreDecoder() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            return null;
        }

        try {
            // Convert the response body to a byte array
            byte[] responseBody = Util.toByteArray(response.body().asInputStream());

            // Create a MapType for the desired type
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            MapType mapType = (MapType) typeFactory.constructType(type);

            // Convert the byte array to the desired map type using Jackson ObjectMapper
            Map<Type, Type> map = objectMapper.readValue(responseBody, mapType);
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

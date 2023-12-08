package org.mangorage.skinmanager.core;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.TextureUrlChecker;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.util.UUIDTypeAdapter;
import org.mangorage.skinmanager.events.TextureUrlCheckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Checker {
    private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    public static Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(MinecraftSessionService sessionService, final GameProfile profile, final boolean requireSecure) throws InsecurePublicKeyException {
        final Property textureProperty = Iterables.getFirst(profile.getProperties().get("textures"), null);
        LOGGER.warn("Called Checker");
        if (textureProperty == null) {
            return new HashMap<>();
        }

        final String value = requireSecure ? sessionService.getSecurePropertyValue(textureProperty) : textureProperty.getValue();

        final MinecraftTexturesPayload result;
        try {
            final String json = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
            result = gson.fromJson(json, MinecraftTexturesPayload.class);
        } catch (final JsonParseException e) {
            LOGGER.error("Could not decode textures payload", e);
            return new HashMap<>();
        }

        if (result == null || result.getTextures() == null) {
            return new HashMap<>();
        }

        for (final Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTexture> entry : result.getTextures().entrySet()) {
            final String url = entry.getValue().getUrl();
            if (!TextureUrlChecker.isAllowedTextureDomain(url)) {
                var event = new TextureUrlCheckEvent(url);
                if (!event.isAllowed()) {
                    LOGGER.error("Textures payload contains blocked domain: {}", url);
                    return new HashMap<>();
                }
            }
        }

        return result.getTextures();
    }

}

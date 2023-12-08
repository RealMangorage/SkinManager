package org.mangorage.skinmanager.core;


import java.net.InetAddress;
import java.util.Map;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;

public class SessionWrapper implements MinecraftSessionService {

    public MinecraftSessionService og;

    public SessionWrapper(MinecraftSessionService og) {
        this.og = og;
    }

    @Override
    public void joinServer(GameProfile profile, String authenticationToken, String serverId)
            throws AuthenticationException {
        og.joinServer(profile, authenticationToken, serverId);
    }

    @Override
    public GameProfile hasJoinedServer(GameProfile user, String serverId, InetAddress address)
            throws AuthenticationUnavailableException {
        return og.hasJoinedServer(user, serverId, address);
    }

    @Override
    public Map<Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure)
            throws InsecurePublicKeyException {
        return Checker.getTextures(og, profile, false);
    }

    @Override
    public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
        return og.fillProfileProperties(profile, false);
    }

    @Override
    public String getSecurePropertyValue(Property property) throws InsecurePublicKeyException {
        return property.getValue();
    }
}

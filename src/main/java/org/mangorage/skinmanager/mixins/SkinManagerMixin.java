package org.mangorage.skinmanager.mixins;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import org.mangorage.skinmanager.Checker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Mixin(SkinManager.class)
public class SkinManagerMixin {

    private final SkinManager skinManager = (SkinManager) (Object) this;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(TextureManager p_118812_, File p_118813_, MinecraftSessionService p_118814_, CallbackInfo ci) {
        this.skinManager.insecureSkinCache = CacheBuilder.newBuilder()
                .expireAfterAccess(15L, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>() {
                    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(String p_118853_) {
                        GameProfile gameprofile = new GameProfile((UUID) null, "dummy_mcdummyface");
                        gameprofile.getProperties().put("textures", new Property("textures", p_118853_, ""));

                        try {
                            return Checker.getTextures(p_118814_, gameprofile, false);
                        } catch (Throwable throwable) {
                            return ImmutableMap.of();
                        }
                    }
                });
    }
}


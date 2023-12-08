package org.mangorage.skinmanager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.mangorage.skinmanager.events.TextureUrlCheckEvent;
import org.slf4j.Logger;

import java.io.File;
import java.util.Base64;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Skinmanager.MODID)
public class Skinmanager {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "skinmanager";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Skinmanager() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayer);
        MinecraftForge.EVENT_BUS.addListener(this::onCheck);
    }

    public void onCheck(TextureUrlCheckEvent event) {
        event.setAllowed();
        LOGGER.debug(event.getUrl());
    }

    public static String getPath(String url) {
        try {
            return new File(url).toURI().toURL().toString();
        } catch (Exception e) {
            return "http://textures.minecraft.net/texture/34b6a48d930567305d41517df699fda48076c184f9d7b09baf8e4d37043cf353";
        }
    }

    // getPath("F:\\Minecraft Forge Projects\\CurioTiab\\test.png");
    public static final String PATH = "https://cdn.discordapp.com/attachments/1129069883813007360/1182356768685293578/test.png";

    private void onPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        setPlayerSkin(event.getEntity());
    }

    public static String getData(String skinURL) {
        return Base64.getEncoder().encodeToString("""
                {
                  "timestamp" : 1701969637965,
                  "profileId" : "4518dce4188841f8b4b5c1de8144f028",
                  "profileName" : "CrazyMangoRage",
                  "textures" : {
                    "SKIN" : {
                      "url" : "%s"
                    }
                  }
                }
                """
                .formatted(
                        skinURL
                )
                .getBytes()
        );
    }

    private void setPlayerSkin(@NotNull Player player) {
        GameProfile gameProfile = player.getGameProfile();



        /*
        CrazyMangoRage:
        ewogICJ0aW1lc3RhbXAiIDogMTcwMTk2OTYzNzk2NSwKICAicHJvZmlsZUlkIiA6ICI0NTE4ZGNlNDE4ODg0MWY4YjRiNWMxZGU4MTQ0ZjAyOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmF6eU1hbmdvUmFnZSIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zNGI2YTQ4ZDkzMDU2NzMwNWQ0MTUxN2RmNjk5ZmRhNDgwNzZjMTg0ZjlkN2IwOWJhZjhlNGQzNzA0M2NmMzUzIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mOWE3NjUzNzY0Nzk4OWY5YTBiNmQwMDFlMzIwZGFjNTkxYzM1OWU5ZTYxYTMxZjRjZTExYzg4ZjIwN2YwYWQ0IgogICAgfQogIH0KfQ==
         */

        // My own Skin
        //var data = "ewogICJ0aW1lc3RhbXAiIDogMTcwMTk2OTYzNzk2NSwKICAicHJvZmlsZUlkIiA6ICI0NTE4ZGNlNDE4ODg0MWY4YjRiNWMxZGU4MTQ0ZjAyOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmF6eU1hbmdvUmFnZSIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zNGI2YTQ4ZDkzMDU2NzMwNWQ0MTUxN2RmNjk5ZmRhNDgwNzZjMTg0ZjlkN2IwOWJhZjhlNGQzNzA0M2NmMzUzIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mOWE3NjUzNzY0Nzk4OWY5YTBiNmQwMDFlMzIwZGFjNTkxYzM1OWU5ZTYxYTMxZjRjZTExYzg4ZjIwN2YwYWQ0IgogICAgfQogIH0KfQ==";

        try {
            // http://textures.minecraft.net/texture/34b6a48d930567305d41517df699fda48076c184f9d7b09baf8e4d37043cf353
            var data = getData(
                    PATH
            );


            gameProfile.getProperties().clear();
            gameProfile.getProperties().put("textures", new Property("textures", data, ""));

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, serverPlayer));
            }

        } catch (Exception a) {

        }
    }
}

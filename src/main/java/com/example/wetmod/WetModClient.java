package com.example.wetmod;

import com.example.wetmod.gui.WetMenuScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class WetModClient implements ClientModInitializer {
    public static KeyMapping openMenuKey;
    public static boolean wetFloorEnabled = false;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("wetmod", "general")
    );

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.wetmod.open_menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_UP,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    client.setScreen(new WetMenuScreen());
                }
            }
        });
    }
}

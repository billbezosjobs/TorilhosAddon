package mdsol.torilhosaddon;

import mdsol.torilhosaddon.events.SetPerspectiveCallback;
import mdsol.torilhosaddon.feature.*;
import mdsol.torilhosaddon.ui.screen.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TorilhosAddonClient implements ClientModInitializer {

    public static final KeyBinding OPEN_CONFIG_KEY = new KeyBinding(
            "key.torilhos-addon.openConfigMenu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.torilhos-addon"
    );

    @Override
    public void onInitializeClient() {
        new FullBrightFeature();
        new NightVisionFeature();
        new HoldToSwingFeature();
        new NexusKeyFeature();
        new AbilityCooldownHudFeature();
        new AbilityRangeFeature();
        new EventBossTrackerHudFeature();
        new HealthBarFeature();
        new WeaponRangeFeature();

        KeyBindingHelper.registerKeyBinding(OPEN_CONFIG_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TorilhosAddonClient.OPEN_CONFIG_KEY.wasPressed()) {
                client.setScreen(new ConfigScreen());
            }
        });

        SetPerspectiveCallback.EVENT.register(perspective -> {
            // TODO: This should probably be moved into a feature.
            // Disable front view perspective.
            if (TorilhosAddon.CONFIG.disableFrontViewPerspective() && perspective == Perspective.THIRD_PERSON_FRONT) {
                return Perspective.FIRST_PERSON;
            }

            return perspective;
        });
    }
}
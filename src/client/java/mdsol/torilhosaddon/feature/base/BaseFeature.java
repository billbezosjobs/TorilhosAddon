package mdsol.torilhosaddon.feature.base;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BaseFeature {

    protected final @NotNull MinecraftClient client;

    protected BaseFeature() {
        client = Objects.requireNonNull(MinecraftClient.getInstance());
    }
}

package mdsol.torilhosaddon.ui.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import mdsol.torilhosaddon.feature.EventBossTrackerHudFeature;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class QuickBossTeleportScreen extends BaseOwoScreen<FlowLayout> {

    private final EventBossTrackerHudFeature eventBossTrackerHudFeature;

    public QuickBossTeleportScreen(EventBossTrackerHudFeature eventBossTrackerHudFeature) {
        this.eventBossTrackerHudFeature = eventBossTrackerHudFeature;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(@NotNull FlowLayout rootComponent) {
        Objects.requireNonNull(client);

        rootComponent
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        var container = Containers.verticalFlow(Sizing.content(), Sizing.content());
        container.padding(Insets.of(16))
                .surface(Surface.DARK_PANEL)
                .horizontalAlignment(HorizontalAlignment.LEFT);
        rootComponent.child(container);

        container.child(
                Components.label(Text.translatable("text.quick-boss-teleport-screen.torilhos-addon.label.teleportTo"))
                        .shadow(true)
                        .margins(Insets.bottom(8))
        );

        var hasPlayersToTeleport = false;

        for (var boss : eventBossTrackerHudFeature.getAliveBosses()) {
            if (boss.getCalledPlayerName() == null) {
                continue;
            }

            hasPlayersToTeleport = true;

            var buttonText = Text.literal("§a" + boss.getData().label)
                    .append("§r [")
                    .append(boss.getCalledPlayerName())
                    .append("]");
            var button = Components.button(
                    buttonText,
                    buttonComponent -> {
                        client.setScreen(null);
                        Objects.requireNonNull(client.getNetworkHandler()).sendCommand("tp " + boss.getCalledPlayerName());
                    }
            ).margins(Insets.top(2));

            container.child(button);
        }

        if (!hasPlayersToTeleport) {
            container.child(
                    Components.label(
                            Text.translatable("text.quick-boss-teleport-screen.torilhos-addon.label.noPlayersFound")
                                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY))
                    )
            );
        }

        container.child(
                Components.button(
                        Text.translatable("text.quick-boss-teleport-screen.torilhos-addon.button.cancel"),
                        buttonComponent -> Objects.requireNonNull(client).setScreen(null)
                ).margins(Insets.top(8))
        );
    }
}

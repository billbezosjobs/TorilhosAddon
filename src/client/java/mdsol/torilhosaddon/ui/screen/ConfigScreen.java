package mdsol.torilhosaddon.ui.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import mdsol.torilhosaddon.TorilhosAddon;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConfigScreen extends BaseOwoScreen<FlowLayout> {

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
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.enableHoldToSwing"))
                                        .checked(TorilhosAddon.CONFIG.enableHoldToSwing())
                                        .onChanged(TorilhosAddon.CONFIG::enableHoldToSwing)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.enableFullBright"))
                                        .checked(TorilhosAddon.CONFIG.enableFullBright())
                                        .onChanged(TorilhosAddon.CONFIG::enableFullBright)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.enableNightVision"))
                                        .checked(TorilhosAddon.CONFIG.enableNightVision())
                                        .onChanged(TorilhosAddon.CONFIG::enableNightVision)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.disableFrontViewPerspective"))
                                        .checked(TorilhosAddon.CONFIG.disableFrontViewPerspective())
                                        .onChanged(TorilhosAddon.CONFIG::disableFrontViewPerspective)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.showEventBossTracker"))
                                        .checked(TorilhosAddon.CONFIG.showEventBossTracker())
                                        .onChanged(TorilhosAddon.CONFIG::showEventBossTracker)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.showAbilityCooldown"))
                                        .checked(TorilhosAddon.CONFIG.showAbilityCooldown())
                                        .onChanged(TorilhosAddon.CONFIG::showAbilityCooldown)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.showHealthBar"))
                                        .checked(TorilhosAddon.CONFIG.showHealthBar())
                                        .onChanged(TorilhosAddon.CONFIG::showHealthBar)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.showWeaponRange"))
                                        .checked(TorilhosAddon.CONFIG.showWeaponRange())
                                        .onChanged(TorilhosAddon.CONFIG::showWeaponRange)
                        )
                        .child(
                                Components.checkbox(Text.translatable("text.config.torilhos-addon.option.showAbilityRange"))
                                        .checked(TorilhosAddon.CONFIG.showAbilityRange())
                                        .onChanged(TorilhosAddon.CONFIG::showAbilityRange)
                        )
                        .child(
                                Components.button(Text.translatable("text.config-screen.torilhos-addon.button.done"), buttonComponent -> Objects.requireNonNull(client).setScreen(null))
                                        .margins(Insets.top(8))
                        )
                        .padding(Insets.of(16))
                        .surface(Surface.DARK_PANEL)
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .verticalAlignment(VerticalAlignment.CENTER)
        );
    }
}

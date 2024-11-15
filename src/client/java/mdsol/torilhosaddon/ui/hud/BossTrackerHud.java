package mdsol.torilhosaddon.ui.hud;

import io.wispforest.owo.ui.base.BaseParentComponent;
import io.wispforest.owo.ui.component.BoxComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.feature.model.TrackedBoss;
import mdsol.torilhosaddon.ui.SimpleItemComponent;
import mdsol.torilhosaddon.ui.hud.base.BaseHudItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BossTrackerHud extends BaseHudItem {

    private final Map<String, BaseParentComponent> aliveBosses = new HashMap<>();
    private final Map<String, LabelComponent> defeatedBosses = new HashMap<>();
    private LabelComponent titleLabel;
    private FlowLayout aliveBossesContainer;
    private FlowLayout defeatedBossesContainer;

    public BossTrackerHud() {
        super(Sizing.content(), Sizing.content(), Algorithm.VERTICAL, TorilhosAddon.id("hud_boss_tracker"));
        positioning(Positioning.relative(0, 0));
        padding(Insets.of(4));
        surface((context, component) -> {
            context.fill(component.x(), component.y(), component.x() + component.width(), component.y() + component.height(), 0xA0101010);
            context.drawBorder(component.x(), component.y(), component.x() + component.width(), component.y() + component.height(), 0xA0AA00AA);
        });
    }

    public void addTrackedBoss(TrackedBoss boss) {
        updateBossComponent(boss);
        boss.observe(this::updateBossComponent);
    }

    public void clear() {
        // Sometimes on the first world join, clear might get called before the hud has been initiated.
        if (!inited) {
            return;
        }

        aliveBossesContainer.clearChildren();
        aliveBosses.clear();
        defeatedBossesContainer.clearChildren();
        defeatedBosses.clear();
        updateTitleLabel();
    }

    public void onWorldJoin(ClientWorld world) {
        aliveBosses.forEach((bossName, parentComponent) -> {
            var component = parentComponent.children().get(1);

            if (!(component instanceof SimpleItemComponent compassComponent)) {
                return;
            }

            var compassStack = compassComponent.getStack();
            var trackerComponent = compassStack.get(DataComponentTypes.LODESTONE_TRACKER);

            if (trackerComponent == null) {
                return;
            }

            trackerComponent.target().ifPresent(globalPos -> {
                var newPos = new GlobalPos(world.getRegistryKey(), globalPos.pos());
                compassStack.set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(Optional.of(newPos), false));
            });
        });
    }

    @Override
    public void init() {
        titleLabel = Components.label(Text.literal("Bosses (?/10)"))
                .shadow(true)
                .color(Color.ofFormatting(Formatting.DARK_PURPLE));

        aliveBossesContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        aliveBossesContainer.margins(Insets.top(2));

        defeatedBossesContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        defeatedBossesContainer.margins(Insets.top(2));

        child(titleLabel);
        child(aliveBossesContainer);
        child(Components.box(Sizing.fixed(60), Sizing.fixed(1))
                .startColor(Color.ofArgb(0xA0AA00AA))
                .endColor(Color.ofArgb(0x00AA00AA))
                .fill(true)
                .direction(BoxComponent.GradientDirection.LEFT_TO_RIGHT)
                .margins(Insets.vertical(4))
        );
        child(defeatedBossesContainer);
    }

    @Override
    public void disable() {
        super.disable();
        aliveBosses.clear();
        defeatedBosses.clear();
    }

    private void updateBossComponent(@NotNull TrackedBoss boss) {
        if (boss.getState() == TrackedBoss.State.ALIVE || boss.getState() == TrackedBoss.State.JUST_DEFEATED) {
            if (defeatedBosses.containsKey(boss.getData().label)) {
                defeatedBosses.remove(boss.getData().label).remove();
            }

            if (aliveBosses.containsKey(boss.getData().label)) {
                updateAliveBossComponent(boss);
                return;
            }

            addAliveBossComponent(boss);
            return;
        }

        if (aliveBosses.containsKey(boss.getData().label)) {
            aliveBosses.remove(boss.getData().label).remove();
        }

        if (!defeatedBosses.containsKey(boss.getData().label)) {
            addDefeatedBossComponent(boss);
        }
    }

    private void updateTitleLabel() {
        titleLabel.text(Text.literal("Bosses (")
                .append(String.valueOf(defeatedBosses.size()))
                .append("/10):"));
    }

    private void addAliveBossComponent(TrackedBoss boss) {
        var container = Containers.horizontalFlow(Sizing.content(), Sizing.content());
        container.verticalAlignment(VerticalAlignment.CENTER);

        container.child(0, Components.label(Text.empty()));
        container.child(1, new SimpleItemComponent(getBossCompassItem(boss)).margins(Insets.right(1)));
        container.child(2, new SimpleItemComponent(getBossHeadItem(boss)).margins(Insets.right(2)));
        container.child(3, Components.label(Text.empty()).shadow(true));

        aliveBosses.put(boss.getData().label, container);
        aliveBossesContainer.child(container);

        updateAliveBossComponent(boss);
    }

    private void updateAliveBossComponent(@NotNull TrackedBoss boss) {
        var container = aliveBosses.get(boss.getData().label);
        var distanceComponent = (LabelComponent) container.children().getFirst();
        var labelColor = boss.getDistanceMarkerValue() <= 2
                         ? Formatting.GREEN
                         : boss.getDistanceMarkerValue() <= 4
                           ? Formatting.YELLOW
                           : boss.getDistanceMarkerValue() <= 6
                             ? Formatting.GOLD
                             : boss.getDistanceMarkerValue() <= 8
                               ? Formatting.RED
                               : Formatting.DARK_RED;
        distanceComponent.text(Text.literal("♦").setStyle(Style.EMPTY.withColor(labelColor)));
        var bossLabelComponent = (LabelComponent) container.children().get(3);
        bossLabelComponent.text(getBossLabelText(boss));
    }

    private void addDefeatedBossComponent(@NotNull TrackedBoss boss) {
        var component = Components.label(Text.literal(boss.getData().label));
        component.shadow(true)
                .color(Color.ofFormatting(Formatting.DARK_RED))
                .margins(Insets.bottom(1));

        defeatedBosses.put(boss.getData().label, component);
        defeatedBossesContainer.child(component);
        updateTitleLabel();
    }

    private @NotNull ItemStack getBossCompassItem(@NotNull TrackedBoss boss) {
        var compass = new ItemStack(Items.COMPASS);
        var dimension = Objects.requireNonNull(client.world).getRegistryKey();
        var bossGlobalPos = new GlobalPos(dimension, boss.getData().spawnPosition);
        var component = new LodestoneTrackerComponent(Optional.of(bossGlobalPos), false);
        compass.set(DataComponentTypes.LODESTONE_TRACKER, component);
        compass.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
        return compass;
    }

    private @NotNull ItemStack getBossHeadItem(@NotNull TrackedBoss boss) {
        var bossHeadItem = new ItemStack(Items.CARROT_ON_A_STICK);
        bossHeadItem.set(DataComponentTypes.CUSTOM_MODEL_DATA, boss.getData().customModelDataComponent);
        return bossHeadItem;
    }

    private @NotNull Text getBossLabelText(@NotNull TrackedBoss boss) {
        var labelText = Text.empty();
        labelText.append(boss.getData().label.substring(0, 3));

        if (boss.getCalledPlayerName() != null) {
            labelText.append(" [").append(boss.getCalledPlayerName()).append("]")
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        }

        if (boss.getState() == TrackedBoss.State.JUST_DEFEATED) {
            labelText.append(" (").append(String.valueOf(boss.getPortalTimer() / 20)).append(")")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED));
        }

        return labelText;
    }
}

package mdsol.torilhosaddon.ui.hud;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.ui.ItemSlotComponent;
import mdsol.torilhosaddon.ui.hud.base.BaseHudItem;
import mdsol.torilhosaddon.util.Items;
import net.minecraft.item.ItemStack;

public class AbilityCooldownHudItem extends BaseHudItem {

    private static final Surface SURFACE_COOLING_DOWN = Surface.flat(0xA0101010).and(Surface.outline(0x90FFFFFF));
    private static final Surface SURFACE_READY = Surface.flat(0xA0FF55FF).and(Surface.outline(0x90FFFFFF));

    private FlowLayout itemSlotContainer;
    private ItemSlotComponent itemSlotComponent;
    private ItemComponent itemComponent;

    private ItemStack previousAbility;
    private float previousCooldownProgress;

    public AbilityCooldownHudItem() {
        super(Sizing.content(), Sizing.content(), Algorithm.VERTICAL, TorilhosAddon.id("hud_off_hand_cooldown"));

        positioning(Positioning.across(50, 50));
        padding(Insets.left(20));
        allowOverflow(true);
    }

    @Override
    public void disable() {
        super.disable();
        previousAbility = ItemStack.EMPTY;
        previousCooldownProgress = -1;
    }

    @Override
    protected void parentUpdate(float delta, int mouseX, int mouseY) {
        super.parentUpdate(delta, mouseX, mouseY);

        var player = client.player;

        if (player == null) {
            return;
        }

        var slotStack = itemSlotComponent.getStack();
        var cooldownProgress = client.player.getItemCooldownManager().getCooldownProgress(slotStack.getItem(), 0);

        if (cooldownProgress != previousCooldownProgress) {
            previousCooldownProgress = cooldownProgress;
            itemSlotContainer.surface(cooldownProgress > 0 ? SURFACE_COOLING_DOWN : SURFACE_READY);
        }

        var currentAbility = Items.getCurrentPlayerAbility();

        if (!Items.isAbility(currentAbility)) {
            // Show no item in the hud.
            itemComponent.stack(ItemStack.EMPTY);
            previousAbility = ItemStack.EMPTY;
            return;
        }

        if (!currentAbility.equals(previousAbility)) {
            // If stack has changed, we update it in the hud.
            previousAbility = currentAbility;
            itemSlotComponent.setStack(currentAbility);
            itemComponent.stack(currentAbility);
        }
    }

    @Override
    public void init() {
        var player = client.player;

        if (player == null) {
            return;
        }

        itemSlotComponent = new ItemSlotComponent(ItemStack.EMPTY);
        var slotVerticalSizing = itemSlotComponent.verticalSizing().get();

        itemSlotContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        itemSlotContainer.surface(SURFACE_READY);
        itemSlotContainer.padding(Insets.of(1));
        itemSlotContainer.child(
                Containers.verticalFlow(Sizing.fixed(4), slotVerticalSizing)
                        .child(itemSlotComponent)
                        .margins(Insets.top((int) (slotVerticalSizing.value * -0.25)))
        );

        itemComponent = Components.item(ItemStack.EMPTY);
        itemComponent.sizing(Sizing.fixed(10)).margins(Insets.left(1));

        child(
                Containers.horizontalFlow(Sizing.content(), Sizing.content())
                        .child(itemSlotContainer)
                        .child(itemComponent)
                        .margins(Insets.top((int) (slotVerticalSizing.value * -0.375)))
        );
    }
}

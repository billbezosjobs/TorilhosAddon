package mdsol.torilhosaddon.ui;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemSlotComponent extends BaseComponent {

    private ItemStack stack;

    public ItemSlotComponent(ItemStack stack) {
        this.stack = stack;
        sizing(Sizing.fixed(16));
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void draw(@NotNull OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawItemInSlot(textRenderer, stack, x, y);
    }
}

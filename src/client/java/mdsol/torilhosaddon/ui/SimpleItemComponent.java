package mdsol.torilhosaddon.ui;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleItemComponent extends BaseComponent {

    private ItemStack stack;

    public SimpleItemComponent(ItemStack stack) {
        this.stack = stack;
        sizing(Sizing.fixed(16));
    }

    @Override
    public void draw(@NotNull OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        context.drawItem(stack, x, y);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}

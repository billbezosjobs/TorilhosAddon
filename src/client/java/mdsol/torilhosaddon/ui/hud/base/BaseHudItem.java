package mdsol.torilhosaddon.ui.hud.base;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public abstract class BaseHudItem extends FlowLayout implements HudItem {

    protected final MinecraftClient client;
    private final Identifier id;
    protected boolean inited = false;

    public BaseHudItem(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm, Identifier id) {
        super(horizontalSizing, verticalSizing, algorithm);
        this.id = id;
        client = MinecraftClient.getInstance();
    }

    public void enable() {
        Hud.add(id, () -> this);
    }

    public void disable() {
        Hud.remove(id);
        clearChildren();
        inited = false;
    }

    @Override
    protected void parentUpdate(float delta, int mouseX, int mouseY) {
        super.parentUpdate(delta, mouseX, mouseY);

        if (!inited) {
            init();
            inited = true;
        }
    }
}

package mdsol.torilhosaddon.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.config.Option;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.feature.base.BaseToggleableFeature;
import mdsol.torilhosaddon.util.Items;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class WeaponRangeFeature extends BaseToggleableFeature {

    private static final Pattern ITEM_RANGE_PATTERN = Pattern.compile("Range: (\\d+(\\.\\d+)?)");

    private ItemStack previousStack = ItemStack.EMPTY;
    private float range = -1;
    private boolean isHoldingWeapon = false;
    private boolean isAbilityRangeEnabled = false;

    public WeaponRangeFeature() {
        super(TorilhosAddon.CONFIG.keys.showWeaponRange);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        WorldRenderEvents.LAST.register(this::onWorldRender);

        // Weapon range feature needs to know if ability range is enabled.
        onAbilityRangeToggle(TorilhosAddon.CONFIG.showAbilityRange());
        Option<Boolean> option = TorilhosAddon.CONFIG.optionForKey(TorilhosAddon.CONFIG.keys.showAbilityRange);
        if (option != null) {
            option.observe(this::onAbilityRangeToggle);
        }
    }

    public void tick(MinecraftClient client) {
        if (!isEnabled()) {
            return;
        }

        var stack = Items.getCurrentPlayerWeapon();

        if (stack.isEmpty()) {
            isHoldingWeapon = false;
            return;
        }

        isHoldingWeapon = true;

        if (stack.equals(previousStack)) {
            return;
        }

        previousStack = stack;
        range = getWeaponRange(stack);
    }

    public void onAbilityRangeToggle(boolean enabled) {
        this.isAbilityRangeEnabled = enabled;
    }

    public void onWorldRender(WorldRenderContext context) {
        if (!isEnabled()
                || !isHoldingWeapon
                || range < 0
                || client.player == null
                || client.player.hasVehicle()
        ) {
            return;
        }

        var camera = context.camera();
        var tickDelta = context.tickCounter().getTickDelta(false);
        var playerPos = new Vec3d(
                MathHelper.lerp(tickDelta, client.player.lastRenderX, client.player.getX()),
                MathHelper.lerp(tickDelta, client.player.lastRenderY, client.player.getY()),
                MathHelper.lerp(tickDelta, client.player.lastRenderZ, client.player.getZ())
        );
        var renderPos = playerPos.subtract(camera.getPos());
        var color = isAbilityRangeEnabled
                    ? 0x00FF0000
                    : Items.getCurrentPlayerAbilityCooldown() > 0 ? 0x00FFFFFF : 0x00AA00AA;

        var matrixStack = Objects.requireNonNull(context.matrixStack());
        matrixStack.push();

        matrixStack.translate(renderPos.x, renderPos.y, renderPos.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));


        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();

        var tessellator = Tessellator.getInstance();
        var circleBuffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        var positionMatrix = matrixStack.peek().getPositionMatrix();
        var angleCount = 6f;

        for (var i = 0; i <= angleCount; i++) {
            var angle = MathHelper.PI * 0.4f + MathHelper.PI * 0.2f * (i / angleCount) - MathHelper.PI * 0.5f;
            var vx = range * MathHelper.sin(angle);
            var vz = range * MathHelper.cos(angle);
            var opacity = 0x80000000;

            if (i == 0 || i == angleCount) {
                opacity = 0;
            }

            circleBuffer.vertex(positionMatrix, vx, 0, vz).color(color + opacity);
            circleBuffer.vertex(positionMatrix, vx, 0.2f, vz).color(color + opacity);
        }

        BufferRenderer.drawWithGlobalProgram(circleBuffer.end());
        RenderSystem.disableBlend();

        matrixStack.pop();
    }

    private float getWeaponRange(@NotNull ItemStack stack) {
        var loreComponent = stack.getComponents().get(DataComponentTypes.LORE);

        if (loreComponent == null) {
            return -1;
        }

        var rangeMatcher = ITEM_RANGE_PATTERN.matcher("");

        for (var line : loreComponent.lines()) {
            rangeMatcher.reset(line.getString());

            if (rangeMatcher.find()) {
                return Float.parseFloat(rangeMatcher.group(1));
            }
        }

        return -1;
    }
}

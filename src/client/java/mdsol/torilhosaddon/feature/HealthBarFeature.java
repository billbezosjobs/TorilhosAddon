package mdsol.torilhosaddon.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.feature.base.BaseToggleableFeature;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class HealthBarFeature extends BaseToggleableFeature {

    public HealthBarFeature() {
        super(TorilhosAddon.CONFIG.keys.showHealthBar);
        WorldRenderEvents.LAST.register(this::onWorldRender);
    }

    public void onWorldRender(WorldRenderContext context) {
        if (!isEnabled()) {
            return;
        }

        if (client.player == null) {
            return;
        }

        var healthPercentage = client.player.getHealth() / client.player.getMaxHealth();

        if (healthPercentage == 1f) {
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
        var healthBarX = -0.6f + healthPercentage * 1.2f;
        var healthBarColor = healthPercentage >= 0.75f
                             ? 0xB040CC40
                             : healthPercentage <= 0.4f
                               ? 0xB0CC3030
                               : 0xB0FFCC40;

        var matrixStack = Objects.requireNonNull(context.matrixStack());

        matrixStack.push();

        matrixStack.translate(renderPos.x, renderPos.y, renderPos.z);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw() + 180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-camera.getPitch()));

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);

        var tessellator = Tessellator.getInstance();
        var positionMatrix = matrixStack.peek().getPositionMatrix();

        var borderBuffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        borderBuffer.vertex(positionMatrix, -0.62f, -0.28f, 0.2f).color(0xB0FFFFFF);
        borderBuffer.vertex(positionMatrix, -0.62f, -0.47f, 0.2f).color(0xB0FFFFFF);
        borderBuffer.vertex(positionMatrix, 0.62f, -0.28f, 0.2f).color(0xB0FFFFFF);
        borderBuffer.vertex(positionMatrix, 0.62f, -0.47f, 0.2f).color(0xB0FFFFFF);
        BufferRenderer.drawWithGlobalProgram(borderBuffer.end());

        var bgBuffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        bgBuffer.vertex(positionMatrix, -0.6f, -0.3f, 0.2f).color(0xB0000000);
        bgBuffer.vertex(positionMatrix, -0.6f, -0.45f, 0.2f).color(0xB0000000);
        bgBuffer.vertex(positionMatrix, 0.6f, -0.3f, 0.2f).color(0xB0000000);
        bgBuffer.vertex(positionMatrix, 0.6f, -0.45f, 0.2f).color(0xB0000000);
        BufferRenderer.drawWithGlobalProgram(bgBuffer.end());

        var fgBuffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        fgBuffer.vertex(positionMatrix, -0.6f, -0.3f, 0.2f).color(healthBarColor);
        fgBuffer.vertex(positionMatrix, -0.6f, -0.45f, 0.2f).color(healthBarColor);
        fgBuffer.vertex(positionMatrix, healthBarX, -0.3f, 0.2f).color(healthBarColor);
        fgBuffer.vertex(positionMatrix, healthBarX, -0.45f, 0.2f).color(healthBarColor);
        BufferRenderer.drawWithGlobalProgram(fgBuffer.end());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        matrixStack.pop();
    }
}

package mdsol.torilhosaddon.mixin.client;

import mdsol.torilhosaddon.events.SetPerspectiveCallback;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow
    private Perspective perspective;

    @Inject(method = "setPerspective", at = @At("TAIL"))
    private void setPerspective(Perspective perspective, CallbackInfo ci) {
        this.perspective = SetPerspectiveCallback.EVENT.invoker().onSetPerspective(perspective);
    }
}

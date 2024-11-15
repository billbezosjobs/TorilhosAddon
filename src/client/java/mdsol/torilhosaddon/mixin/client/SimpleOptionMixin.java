package mdsol.torilhosaddon.mixin.client;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Try to improve this to not have to cancel the methods.
@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> {

    @Shadow
    @Final
    Text text;

    @Shadow
    T value;

    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    private void getCodec(CallbackInfoReturnable<Codec<Double>> cir) {
        if (isGammaOption()) {
            cir.setReturnValue(Codec.DOUBLE);
        }
    }

    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    private void setValue(T value, CallbackInfo ci) {
        if (isGammaOption()) {
            this.value = value;
            ci.cancel();
        }
    }

    @Unique
    private boolean isGammaOption() {
        return text.getString().equals(I18n.translate("options.gamma"));
    }
}

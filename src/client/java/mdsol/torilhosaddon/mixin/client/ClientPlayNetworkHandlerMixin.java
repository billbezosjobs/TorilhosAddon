package mdsol.torilhosaddon.mixin.client;

import mdsol.torilhosaddon.events.NetworkHandlerOnGameJoin;
import mdsol.torilhosaddon.events.NetworkHandlerOnPlayerAbilities;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onPlayerAbilities", at = @At("TAIL"))
    private void onPlayerAbilities(PlayerAbilitiesS2CPacket packet, CallbackInfo ci) {
        NetworkHandlerOnPlayerAbilities.EVENT.invoker().onPlayerAbilities();
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        NetworkHandlerOnGameJoin.EVENT.invoker().onGameJoin();
    }
}

package mdsol.torilhosaddon.feature;

import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.events.NetworkHandlerOnPlayerAbilities;
import mdsol.torilhosaddon.feature.base.BaseToggleableFeature;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class NightVisionFeature extends BaseToggleableFeature {

    public NightVisionFeature() {
        super(TorilhosAddon.CONFIG.keys.enableNightVision);
        NetworkHandlerOnPlayerAbilities.EVENT.register(() -> {
            if (isEnabled()) {
                enableNightVision();
            }
        });
    }

    @Override
    protected void onEnable() {
        enableNightVision();
    }

    @Override
    protected void onDisable() {
        disableNightVision();
    }

    private void enableNightVision() {
        if (client.player != null) {
            client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1));
        }
    }

    private void disableNightVision() {
        if (client.player != null) {
            client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}

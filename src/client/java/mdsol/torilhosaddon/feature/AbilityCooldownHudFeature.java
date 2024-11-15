package mdsol.torilhosaddon.feature;

import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.feature.base.BaseHudFeature;
import mdsol.torilhosaddon.ui.hud.AbilityCooldownHudItem;

public class AbilityCooldownHudFeature extends BaseHudFeature {

    public AbilityCooldownHudFeature() {
        super(TorilhosAddon.CONFIG.keys.showAbilityCooldown, new AbilityCooldownHudItem());
    }
}

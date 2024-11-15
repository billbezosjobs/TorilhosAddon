package mdsol.torilhosaddon.util;

import io.wispforest.owo.config.Option;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.feature.base.ToggleableFeature;

public class Configs {

    public static void bindFeatureToggle(Option.Key configKey, ToggleableFeature toggleableFeature) {
        Option<Boolean> option = TorilhosAddon.CONFIG.optionForKey(configKey);
        if (option != null) {
            option.observe(toggleableFeature::setEnabled);
        }
    }
}

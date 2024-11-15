package mdsol.torilhosaddon.feature.base;

import io.wispforest.owo.config.Option;
import mdsol.torilhosaddon.TorilhosAddon;
import mdsol.torilhosaddon.util.Configs;

import java.util.Objects;

public class BaseToggleableFeature extends BaseFeature implements ToggleableFeature {

    private boolean enabled;

    protected BaseToggleableFeature(Option.Key configKey) {
        super();
        setEnabled((Boolean) Objects.requireNonNull(TorilhosAddon.CONFIG.optionForKey(configKey)).value());
        Configs.bindFeatureToggle(configKey, this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
            return;
        }

        onDisable();
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }
}

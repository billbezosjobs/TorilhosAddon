package mdsol.torilhosaddon;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = TorilhosAddon.MOD_ID)
@Config(name = TorilhosAddon.MOD_ID, wrapperName = "ModConfig")
public class ModConfigModel {

    public boolean enableHoldToSwing = false;
    public boolean enableFullBright = false;
    public boolean enableNightVision = false;
    public boolean disableFrontViewPerspective = false;
    public boolean showEventBossTracker = false;
    public boolean showAbilityCooldown = false;
    public boolean showHealthBar = false;
    public boolean showWeaponRange = false;
    public boolean showAbilityRange = false;
}

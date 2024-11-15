package mdsol.torilhosaddon.feature.model;

import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

public enum BossData {
    ANUBIS(
            "Anubis",
            Pattern.compile("^\\[Anubis] Kneel before your final reckoning, (.+)!"),
            new BlockPos(458, 0, -467),
            new CustomModelDataComponent(829068)
    ),
    ASTAROTH(
            "Astaroth",
            Pattern.compile("^\\[Astaroth] Your futile struggles are mere entertainment for the denizens of the void, (.+)"),
            new BlockPos(250, 0, 60),
            new CustomModelDataComponent(829064)
    ),
    CHUNGUS(
            "Chungus",
            Pattern.compile("^\\[Chungus] The void strengthens me, (.+)!"),
            new BlockPos(61, 0, -490),
            new CustomModelDataComponent(829066)
    ),
    FREDDY(
            "Freddy",
            Pattern.compile("^\\[Freddy] YOU WILL NOT BE SPARED! YOU WILL NOT BE SAVED, (.+)!"),
            new BlockPos(-136, 0, 653),
            new CustomModelDataComponent(829067)
    ),
    GLUMI(
            "Glumi",
            Pattern.compile("^\\[Glumi] You will not access the sacred caverns, (.+)!"),
            new BlockPos(339, 0, 552),
            new CustomModelDataComponent(829063)
    ),
    ILLARIUS(
            "Illarius",
            Pattern.compile("^\\[Illarius] Don't send me back to Loa, (.+)!"),
            new BlockPos(478, 0, -45),
            new CustomModelDataComponent(829065)
    ),
    LOTIL(
            "Lotil",
            Pattern.compile("^\\[Lotil] You will NOT take my symbolic shield away from me, (.+)!"),
            new BlockPos(-138, 0, 17),
            new CustomModelDataComponent(829059)
    ),
    OOZUL(
            "Oozul",
            Pattern.compile("^\\[Oozul] Don't expose mortals such as (.+) to Chronos!"),
            new BlockPos(-424, 0, 91),
            new CustomModelDataComponent(829060)
    ),
    TIDOL(
            "Tidol",
            Pattern.compile("^\\[Tidol] Face my trident, (.+)!"),
            new BlockPos(-543, 0, 364),
            new CustomModelDataComponent(829062)
    ),
    VALUS(
            "Valus",
            Pattern.compile("^\\[Valus] You are not worthy of joining our worship, (.+)!"),
            new BlockPos(35, 0, 307),
            new CustomModelDataComponent(829061)
    ),
    HOLLOWBANE(
            "Hollowbane",
            Pattern.compile("^\\[Hollowbane] Hollow is your fate, as it is mine (.+)!"),
            new BlockPos(232, 0, 696),
            new CustomModelDataComponent(829103)
    );

    public final String label;
    public final @Nullable Pattern playerCallPattern;
    public final BlockPos spawnPosition;
    public final CustomModelDataComponent customModelDataComponent;

    BossData(String label, @Nullable Pattern playerCallPattern, BlockPos spawnPosition, CustomModelDataComponent customModelDataComponent) {
        this.label = label;
        this.playerCallPattern = playerCallPattern;
        this.spawnPosition = spawnPosition;
        this.customModelDataComponent = customModelDataComponent;
    }

    public static Optional<BossData> fromString(@NotNull String name) {
        try {
            return Optional.of(valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}

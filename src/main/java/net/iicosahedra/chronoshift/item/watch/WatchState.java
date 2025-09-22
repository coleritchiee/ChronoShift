package net.iicosahedra.chronoshift.item.watch;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record WatchState(int tier, List<ResourceLocation> upgrades) {
    public static final WatchState DEFAULT = new WatchState(1, List.of());

    public static final Codec<WatchState> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("tier").forGetter(WatchState::tier),
            ResourceLocation.CODEC.listOf().fieldOf("upgrades").forGetter(WatchState::upgrades)
    ).apply(i, WatchState::new));

    public WatchState withTier(int newTier) {
        return new WatchState(newTier, upgrades);
    }
}

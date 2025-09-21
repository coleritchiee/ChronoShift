package net.iicosahedra.chronoshift.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public record TraitData(Set<ResourceLocation> traits) {
    public static final Codec<TraitData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("traits").forGetter(t -> t.traits)
    ).apply(instance, instance.stable(TraitData::new)));
}

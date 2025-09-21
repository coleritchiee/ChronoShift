package net.iicosahedra.chronoshift.util;

import net.iicosahedra.chronoshift.ChronoShift;
import net.minecraft.resources.ResourceLocation;

public class ResourceLoc {
    public static ResourceLocation create(String path){
        return ResourceLocation.fromNamespaceAndPath(ChronoShift.MODID, path);
    }
}

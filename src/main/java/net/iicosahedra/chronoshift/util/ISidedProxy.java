package net.iicosahedra.chronoshift.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface ISidedProxy {
    @Nullable
    Player getClientPlayer();
    @Nullable
    ClientLevel getClientWorld();
    @Nullable
    Minecraft getInstance();
}

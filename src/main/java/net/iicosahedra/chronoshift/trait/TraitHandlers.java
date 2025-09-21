package net.iicosahedra.chronoshift.trait;

import net.iicosahedra.chronoshift.ChronoShift;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

@EventBusSubscriber(modid = ChronoShift.MODID)
public class TraitHandlers {
    @SubscribeEvent
    public static void onSpawn(FinalizeSpawnEvent event) {
        if(event.getEntity() instanceof Monster && !event.getLevel().isClientSide()) {
            ServerPlayer source = TraitHelper.findGreatestWatchHolder(event.getLevel().getLevel(), event.getEntity().blockPosition(), 32);

            if(source != null) {
                event.getEntity().setCustomName(Component.literal("test"));
            }
        }
    }
}

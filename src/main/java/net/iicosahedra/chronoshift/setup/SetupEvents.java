package net.iicosahedra.chronoshift.setup;

import net.iicosahedra.chronoshift.ChronoShift;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = ChronoShift.MODID)
public class SetupEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    static void registerAttributes(EntityAttributeModificationEvent event) {

    }

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event){

    }
}

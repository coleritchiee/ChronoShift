package net.iicosahedra.chronoshift.trait;

import net.iicosahedra.chronoshift.ChronoShift;
import net.iicosahedra.chronoshift.item.watch.WatchHelper;
import net.iicosahedra.chronoshift.setup.Registration;
import net.iicosahedra.chronoshift.util.ResourceLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = ChronoShift.MODID)
public class TraitHandlers {
    @SubscribeEvent
    public static void onSpawn(FinalizeSpawnEvent event) {
        if(event.getEntity() instanceof Monster && !event.getLevel().isClientSide()) {
            ServerPlayer source = TraitHelper.findGreatestWatchHolder(event.getLevel().getLevel(), event.getEntity().blockPosition(), 32);
            if(source != null) {
                int tier = WatchHelper.getWatchFromSlot(source, Registration.TIME_WATCH.value()).get().get(Registration.WATCH_STATE).tier();
                Set<Trait> traits = new HashSet<>();
                for(int i = 0; i < tier; i++) {
                    Trait trait = Registration.getRandomRegistryObject(Registration.TRAITS);
                    while (tier < trait.getTier()) {
                        trait = Registration.getRandomRegistryObject(Registration.TRAITS);
                    }
                    traits.add(trait);
                }
                for(Trait trait : traits) {
                    event.getEntity().setData(Registration.TRAIT_DATA, new TraitData(Set.of(Registration.TRAIT_REGISTRY.getResourceKey(trait).get().location())));

                    //TODO: Detach from nameplate to separate display

                    event.getEntity().setCustomName(trait.getDisplayName());
                    event.getEntity().setCustomNameVisible(true);

                    for(AttributeHolder attribute : trait.getAttributes()){
                        attribute.applyModifier(event.getEntity(), event.getEntity().getStringUUID()+trait.getName());
                    }
                }
                //TODO: Attach model change
            }
        }
    }

    @SubscribeEvent
    public static void onDrop(LivingDropsEvent event) {
        if(event.getEntity().hasData(Registration.TRAIT_DATA)) {
            Set<ResourceLocation> traitKeys = event.getEntity().getData(Registration.TRAIT_DATA).traits();
            Set<Trait> traits = new HashSet<>();
            for(ResourceLocation traitKey : traitKeys){
                traits.add(Registration.TRAIT_REGISTRY.get(traitKey));
            }

            if(!traits.isEmpty() && !traits.contains(null)) {
                for(Trait trait : traits){
                    if(event.getEntity().getRandom().nextDouble() <= 0.1) {
                        ItemStack stack = new ItemStack(trait.getDroppedItem());
                        ItemEntity entity = new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), stack);
                        entity.setDefaultPickUpDelay();
                        event.getDrops().add(entity);
                    }
                }
            }
        }
    }
}

package net.iicosahedra.chronoshift.setup;

import net.iicosahedra.chronoshift.ChronoShift;
import net.iicosahedra.chronoshift.item.watch.WatchState;
import net.iicosahedra.chronoshift.trait.TraitData;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class Registration {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChronoShift.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChronoShift.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChronoShift.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChronoShift.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, ChronoShift.MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ChronoShift.MODID);

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        BLOCKS.register(modEventBus);
        CREATIVE_TAB.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        COMPONENTS.register(modEventBus);
    }

    public static <T> T getRandomRegistryObject(DeferredRegister<T> registry){
        List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>(registry.getEntries());
        Random rand = new Random();
        int index = rand.nextInt(entries.size());
        return entries.get(index).get();
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponent(
            String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator){
        return COMPONENTS.register(name, ()-> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }

    //Attachments
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TraitData>> TRAIT_DATA =
            ATTACHMENT_TYPES.register("trait_data", ()->
                    AttachmentType.<TraitData>builder(()-> new TraitData(Set.of()))
                            .serialize(TraitData.CODEC)
                            .build());

    //Watch Component
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WatchState>> WATCH_STATE =
            COMPONENTS.registerComponentType("watch_state", builder -> builder
                    .persistent(WatchState.CODEC)
                    .networkSynchronized(StreamCodec.of(
                            Registration::encodeWatch, Registration::decodeWatch))
            );

    private static void encodeWatch(FriendlyByteBuf buf, WatchState ws) {
        buf.writeVarInt(ws.tier());
        buf.writeVarInt(ws.upgrades().size());
        for (var rl : ws.upgrades()) buf.writeResourceLocation(rl);
    }
    private static WatchState decodeWatch(FriendlyByteBuf buf) {
        int tier = buf.readVarInt();
        int n = buf.readVarInt();
        java.util.ArrayList<net.minecraft.resources.ResourceLocation> ups = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) ups.add(buf.readResourceLocation());
        return new WatchState(tier, java.util.List.copyOf(ups));
    }

    //Item
    public static final Holder<Item> TIME_WATCH =
            ITEMS.register("time_watch", () -> new Item(new Item.Properties()
                    .component(WATCH_STATE.value(), WatchState.DEFAULT)
            ));
}
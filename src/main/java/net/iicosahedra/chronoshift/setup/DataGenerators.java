package net.iicosahedra.chronoshift.setup;

import net.iicosahedra.chronoshift.ChronoShift;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ChronoShift.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(packOutput, lookupProvider, helper);
        gen.addProvider(event.includeServer(), blockTagProvider);
        gen.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), helper));

        gen.addProvider(event.includeClient(), new ModBlockStates(gen, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "en_us"));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, helper));
        gen.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput, event.getLookupProvider()));
        //gen.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

}
class ModBlockStates extends BlockStateProvider {
    public ModBlockStates(DataGenerator gen, ExistingFileHelper helper){
        super(gen.getPackOutput(), ChronoShift.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels(){

    }
}

class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BROKEN_MOVEMENT.value(), 1)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Registration.TEMPORAL_SHARD.value())
                .define('B', Items.IRON_INGOT)
                .define('C', Items.COMPARATOR)
                .group(ChronoShift.MODID)
                .unlockedBy(getHasName(Registration.TEMPORAL_SHARD.value()), has(Registration.TEMPORAL_SHARD.value()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.TIME_WATCH.value(), 1)
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Registration.TEMPORAL_SHARD.value())
                .define('B', Items.DIAMOND)
                .define('C', Registration.BROKEN_MOVEMENT.value())
                .group(ChronoShift.MODID)
                .unlockedBy(getHasName(Registration.BROKEN_MOVEMENT.value()), has(Registration.BROKEN_MOVEMENT.value()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BLOOD_CIRCUIT.value(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Registration.CRYSTAL_BLOOD.value())
                .define('B', Registration.PROCESSOR.value())
                .group(ChronoShift.MODID)
                .unlockedBy(getHasName(Registration.CRYSTAL_BLOOD.value()), has(Registration.CRYSTAL_BLOOD.value()))
                .save(recipeOutput);
    }
}


class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChronoShift.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(Registration.TEMPORAL_SHARD.value());
        handheldItem(Registration.BROKEN_MOVEMENT.value());
        handheldItem(Registration.CRYSTAL_BLOOD.value());
        handheldItem(Registration.TIME_WATCH.value());
        handheldItem(Registration.ENHANCED_LENS.value());
        handheldItem(Registration.PROCESSOR.value());
        handheldItem(Registration.BLOOD_CIRCUIT.value());
    }
}

class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ChronoShift.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }
}

class ModBlockLootTableProvider extends BlockLootSubProvider{
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}

class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, ChronoShift.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(Registration.BROKEN_MOVEMENT.value(), "Broken Movement");
        add(Registration.CRYSTAL_BLOOD.value(), "Crystal Blood");
        add(Registration.TIME_WATCH.value(), "Time Watch");
        add(Registration.TEMPORAL_SHARD.value(), "Temporal Shard");
        add(Registration.ENHANCED_LENS.value(), "Enhanced Lens");
        add(Registration.PROCESSOR.value(), "Processor");
        add(Registration.BLOOD_CIRCUIT.value(), "Blood Circuit");
    }
}

class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ChronoShift.MODID);
    }

    @Override
    protected void start() {
    }
}

/*
class ModWorldGenProvider extends DatapackBuiltinEntriesProvider{
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()


    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries,BUILDER, Set.of(Beyonder.MODID));
    }
}
 */

class ModBlockTagProvider extends IntrinsicHolderTagsProvider<Block> {

    @SuppressWarnings("deprecation")
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key(), ChronoShift.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }
}

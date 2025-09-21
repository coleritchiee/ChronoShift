package net.iicosahedra.chronoshift;

import com.mojang.logging.LogUtils;
import net.iicosahedra.chronoshift.setup.Registration;
import net.iicosahedra.chronoshift.util.ClientSidedProxy;
import net.iicosahedra.chronoshift.util.ISidedProxy;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(ChronoShift.MODID)
public class ChronoShift {
    public static final String MODID = "chronoshift";

    public static ChronoShift instance;

    public ISidedProxy proxy;
    private static final Logger LOGGER = LogUtils.getLogger();

    public ChronoShift(IEventBus modEventBus, ModContainer modContainer)
    {
        instance = this;
        proxy = new ClientSidedProxy();
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        Registration.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

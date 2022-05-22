package ca.lukegrahamlandry.eternalartifacts;

import ca.lukegrahamlandry.eternalartifacts.config.FishingXpValues;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperienceImpl;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import ca.lukegrahamlandry.eternalartifacts.registry.BlockInit;
import ca.lukegrahamlandry.eternalartifacts.registry.ItemInit;
import ca.lukegrahamlandry.eternalartifacts.registry.TileTypeInit;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "eternalartifacts";

    public static final Logger LOGGER = LogManager.getLogger();

    public static FishingXpValues FISHING_XP_CONFIG;
    public static MinecraftServer server;

    public ModMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        TileTypeInit.TILE_ENTITY_TYPES.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        GeckoLib.initialize();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ArtifactExperience.class, new ArtifactXpCapability.Storage(), ArtifactExperienceImpl::new);
        NetworkInit.registerPackets();
    }
}

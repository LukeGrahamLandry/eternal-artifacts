package ca.lukegrahamlandry.eternalartifacts.events;


import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.config.FishingXpValues;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {
    @SubscribeEvent
    public static void initCaps(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity){
            event.addCapability(new ResourceLocation(ModMain.MOD_ID, "artifactxp"), new ArtifactXpCapability());
        }
    }

    @SubscribeEvent
    public static void copyCaps(PlayerEvent.Clone event){
        event.getOriginal().getCapability(ArtifactXpCapability.CAP).ifPresent((originalCap -> {
            event.getEntityLiving().getCapability(ArtifactXpCapability.CAP).ifPresent((newCap -> {
                newCap.read(originalCap.write());
            }));
        }));
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getEntityLiving().level.isClientSide()){

        }
    }
}
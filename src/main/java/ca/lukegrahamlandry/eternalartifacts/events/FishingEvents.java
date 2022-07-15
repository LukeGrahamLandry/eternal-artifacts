package ca.lukegrahamlandry.eternalartifacts.events;


import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.config.FishingXpValues;
import ca.lukegrahamlandry.eternalartifacts.config.SkillsConfig;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.ExperienceUpdatePacket;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.SyncJsonConfigPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FishingEvents {
    @SubscribeEvent
    public static void serverInit(final FMLServerStartingEvent event) {
        ModMain.server = event.getServer();
        ModMain.loadConfigs();
    }

    @SubscribeEvent
    public static void onFish(ItemFishedEvent event){
        if (!event.getEntityLiving().level.isClientSide()){
            ArtifactXpCapability.ifPresent(event.getPlayer(), artifactExperience -> {
                int xp = 0;
                for (ItemStack stack : event.getDrops()){
                    xp += ModMain.FISHING_XP_CONFIG.getXpValue(stack.getItem()) * stack.getCount();
                }
                System.out.println("fishing xp " + xp);
                if (xp > 0) {
                    artifactExperience.addExperience(ArtifactExperience.FISHING, xp);
                    NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new ExperienceUpdatePacket(ArtifactExperience.FISHING, artifactExperience.getExperience(ArtifactExperience.FISHING), ModMain.FISHING_XP_CONFIG.xpDisplayRatio()));
                    artifactExperience.sync(event.getPlayer());
                }
            });
        }
    }

    @SubscribeEvent
    public static void syncXp(PlayerEvent.PlayerLoggedInEvent event){
        if (!event.getEntityLiving().level.isClientSide()){
            ArtifactXpCapability.ifPresent(event.getPlayer(), artifactExperience -> {
                NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new ExperienceUpdatePacket(ArtifactExperience.FISHING, artifactExperience.getExperience(ArtifactExperience.FISHING), ModMain.FISHING_XP_CONFIG.xpDisplayRatio()));
                artifactExperience.sync(event.getPlayer());
            });

            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), SyncJsonConfigPacket.fishingSkills(ModMain.FISHING_SKILL_STATS.data));
        }
    }
}

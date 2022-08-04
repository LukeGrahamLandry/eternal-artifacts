package ca.lukegrahamlandry.eternalartifacts.events;


import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.leveling.SkillType;
import ca.lukegrahamlandry.eternalartifacts.leveling.skillimpl.WaterCombatSkillStats;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.ExperienceUpdatePacket;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.SyncJsonConfigPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FishingArtifactSkillEvents {
    @SubscribeEvent
    public static void handleAttack(LivingDamageEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) attacker;
            int lvl = SkillType.STRENGTH_IN_WATER.getActiveLevel(player);
            if (lvl > 0 && player.isInWater()){
                WaterCombatSkillStats stats = (WaterCombatSkillStats) SkillType.STRENGTH_IN_WATER.getStats();
                float scale = 1 + stats.getScaleFactor(lvl);
                event.setAmount(event.getAmount() * scale);
            }
        }
    }

    @SubscribeEvent
    public static void handleEat(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            int lvl = SkillType.STRENGTH_IN_WATER.getActiveLevel(player);
            if (lvl > 0 && event.getItem().getItem().is(ItemTags.FISHES)){
                WaterCombatSkillStats stats = (WaterCombatSkillStats) SkillType.STRENGTH_IN_WATER.getStats();
                for (EffectInstance effect : stats.getEffects(lvl)){
                    player.addEffect(effect);
                }
            }
        }
    }
}

package ca.lukegrahamlandry.eternalartifacts.leveling;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface ArtifactExperience {
    ResourceLocation FISHING = new ResourceLocation(ModMain.MOD_ID, "fishing");
    ResourceLocation[] ARTIFACT_TYPES = new ResourceLocation[]{FISHING};

    int getExperience(ResourceLocation type);
    int getTotalExperience(ResourceLocation type);
    void addExperience(ResourceLocation type, int amount);
    boolean spendExperience(ResourceLocation type, int amount);

    boolean hasSkill(ResourceLocation artifact, ResourceLocation skill);
    int getSkillLevel(ResourceLocation artifact, ResourceLocation skill);
    void learnSkill(ResourceLocation artifact, ResourceLocation skill, int level);
    Map<ResourceLocation, Integer> getSkills(ResourceLocation artifact);

    CompoundNBT write();
    void read(CompoundNBT tag);

    void sync(PlayerEntity player);

    int getXpDisplayRatio(ResourceLocation resourceLocation);
}

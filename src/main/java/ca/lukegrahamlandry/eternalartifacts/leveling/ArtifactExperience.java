package ca.lukegrahamlandry.eternalartifacts.leveling;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface ArtifactExperience {
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
}

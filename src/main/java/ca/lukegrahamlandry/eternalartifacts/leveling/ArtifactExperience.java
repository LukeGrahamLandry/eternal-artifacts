package ca.lukegrahamlandry.eternalartifacts.leveling;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface ArtifactExperience {
    ResourceLocation FISHING = new ResourceLocation(ModMain.MOD_ID, "fishing");
    ResourceLocation[] ARTIFACT_TYPES = new ResourceLocation[]{FISHING};

    // how much experience currently available to spend
    int getExperience(ResourceLocation type);

    // how much experience you have ever collected
    int getTotalExperience(ResourceLocation type);

    // add experience to current and total
    void addExperience(ResourceLocation type, int amount);

    // reduce available experience
    boolean spendExperience(ResourceLocation type, int amount);

    boolean hasSkill(ResourceLocation artifact, ResourceLocation skill);
    int getSkillLevel(ResourceLocation artifact, ResourceLocation skill);
    void learnSkill(ResourceLocation artifact, ResourceLocation skill, int level);
    Map<ResourceLocation, Integer> getSkills(ResourceLocation artifact);

    CompoundNBT write();
    void read(CompoundNBT tag);

    // send data to client
    void sync(PlayerEntity player);

    // how many xp per level
    int getXpDisplayRatio(ResourceLocation resourceLocation);

    // the sum of your level in each skill
    int getArtifactLevel(ResourceLocation fishing);
}

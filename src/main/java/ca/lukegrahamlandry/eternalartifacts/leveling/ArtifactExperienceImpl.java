package ca.lukegrahamlandry.eternalartifacts.leveling;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.SyncArtifactCapabilityPacket;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class ArtifactExperienceImpl implements ArtifactExperience {
    Map<ResourceLocation, Integer> xp = new HashMap<>();
    Map<ResourceLocation, Integer> totalXp = new HashMap<>();
    Map<ResourceLocation, Map<ResourceLocation, Integer>> skills = new HashMap<>();

    @Override
    public int getExperience(ResourceLocation type) {
        return xp.getOrDefault(type, 0);
    }

    @Override
    public int getTotalExperience(ResourceLocation type) {
        return totalXp.getOrDefault(type, 0);
    }

    @Override
    public void addExperience(ResourceLocation type, int amount) {
        xp.put(type, xp.getOrDefault(type, 0) + amount);
        totalXp.put(type, totalXp.getOrDefault(type, 0) + amount);
    }

    @Override
    public boolean spendExperience(ResourceLocation type, int amount) {
        if (xp.getOrDefault(type, 0) < amount) return false;
        xp.put(type, xp.getOrDefault(type, 0) - amount);
        return true;
    }



    @Override
    public boolean hasSkill(ResourceLocation artifact, ResourceLocation skill) {
        return getSkillLevel(artifact, skill) != 0;
    }

    @Override
    public int getSkillLevel(ResourceLocation artifact, ResourceLocation skill) {
        return skills.containsKey(artifact) && skills.get(artifact).containsKey(skill) ? skills.get(artifact).get(skill) : 0;
    }

    @Override
    public void learnSkill(ResourceLocation artifact, ResourceLocation skill, int level) {
        if (!skills.containsKey(artifact)) skills.put(artifact, new HashMap<>());
        skills.get(artifact).put(skill, level);
    }

    @Override
    public Map<ResourceLocation, Integer> getSkills(ResourceLocation artifact) {
        return ImmutableMap.copyOf(skills.get(artifact).entrySet());
    }

    private CompoundNBT writeIntMap(Map<ResourceLocation, Integer> data){
        CompoundNBT tag = new CompoundNBT();
        for (ResourceLocation rl : data.keySet()){
            tag.putInt(rl.toString(), data.get(rl));
        }
        return tag;
    }

    private Map<ResourceLocation, Integer> readIntMap(CompoundNBT tag){
        Map<ResourceLocation, Integer> data = new HashMap<>();
        for (String key : tag.getAllKeys()){
            data.put(new ResourceLocation(key), tag.getInt(key));
        }
        return data;
    }

    @Override
    public CompoundNBT write() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("xp", writeIntMap(xp));
        tag.put("totalxp", writeIntMap(totalXp));
        CompoundNBT skillsData = new CompoundNBT();
        for (ResourceLocation artifact : skills.keySet()){
            skillsData.put(artifact.toString(), writeIntMap(skills.get(artifact)));
        }
        tag.put("skills", skillsData);
        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {
        xp = tag.contains("xp") ? readIntMap(tag.getCompound("xp")) : new HashMap<>();
        totalXp = tag.contains("totalxp") ? readIntMap(tag.getCompound("totalxp")) : new HashMap<>();
        skills = new HashMap<>();
        if (tag.contains("skills")){
            CompoundNBT skillsData = tag.getCompound("skills");
            for (String key : skillsData.getAllKeys()){
                skills.put(new ResourceLocation(key), readIntMap(skillsData.getCompound(key)));
            }
        }
    }

    @Override
    public void sync(PlayerEntity player) {
        if (!player.level.isClientSide()){
            NetworkInit.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new SyncArtifactCapabilityPacket(this));
        }
    }

    @Override
    public int getXpDisplayRatio(ResourceLocation type) {
        if (FISHING.equals(type)) return ModMain.FISHING_XP_CONFIG.xpDisplayRatio();
        return 0;
    }
}

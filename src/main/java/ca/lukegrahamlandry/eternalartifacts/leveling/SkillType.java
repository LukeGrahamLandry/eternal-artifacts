package ca.lukegrahamlandry.eternalartifacts.leveling;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.leveling.skillimpl.AirAmountStats;
import ca.lukegrahamlandry.eternalartifacts.leveling.skillimpl.WaterCombatSkillStats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public enum SkillType {
    EXTRA_HEARTS,
    STRENGTH_IN_WATER(WaterCombatSkillStats.class),
    WALK_ON_WATER,
    MORE_AIR(AirAmountStats.class);

    public final Class<? extends SkillStats> clazz;
    SkillType(Class<? extends SkillStats> clazz){
        this.clazz = clazz;
    }

    SkillType(){
        this(SkillStats.class);
    }

    public static SkillType getSkill(ResourceLocation skill) {
        return valueOf(skill.getPath().toUpperCase(Locale.ROOT));
    }

    public SkillStats getStats(){
        return ModMain.FISHING_SKILL_STATS.skillStats.get(this);
    }

    public ResourceLocation getKey(){
        return new ResourceLocation(ModMain.MOD_ID, this.name().toLowerCase(Locale.ROOT));
    }


    // todo: handle different artifacts properly
    // todo: make them be holding the artifact item
    // todo: let them temperarily disable skill in gui
    public int getActiveLevel(PlayerEntity player){
        if (player == null) return 0;

        ArtifactExperience xp = player.getCapability(ArtifactXpCapability.CAP).orElse(new ArtifactExperienceImpl());
        return xp.getSkillLevel(ArtifactExperience.FISHING, this.getKey());
    }
}

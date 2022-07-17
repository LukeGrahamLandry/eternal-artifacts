package ca.lukegrahamlandry.eternalartifacts.leveling;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public enum SkillType {
    EXTRA_HEARTS,
    STRENGTH_IN_WATER,
    WALK_ON_WATER;

    public final Class<SkillStats> clazz;
    SkillType(Class<SkillStats> clazz){
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


}

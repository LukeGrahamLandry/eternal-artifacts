package ca.lukegrahamlandry.eternalartifacts.registry;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.config.SkillsConfig;

public enum SkillType {
    EXTRA_HEARTS,
    STRENGTH_IN_WATER,
    WALK_ON_WATER;

    public final Class<SkillsConfig.SkillStats> clazz;
    SkillType(Class<SkillsConfig.SkillStats> clazz){
        this.clazz = clazz;
    }

    SkillType(){
        this(SkillsConfig.SkillStats.class);
    }

    public SkillsConfig.SkillStats getStats(){
        return ModMain.FISHING_SKILL_STATS.skillStats.get(this);
    }
}

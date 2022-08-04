package ca.lukegrahamlandry.eternalartifacts.leveling.skillimpl;

import ca.lukegrahamlandry.eternalartifacts.leveling.SkillStats;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class WaterCombatSkillStats extends SkillStats {
    public Float[] damageScaleFactor;
    public String[] effects;

    public float getScaleFactor(int lvl) {
        if (lvl >= damageScaleFactor.length) lvl = damageScaleFactor.length - 1;
        return damageScaleFactor[lvl-1];
    }

    private static final int effectLength = 30*20;
    public List<EffectInstance> getEffects(int lvl) {
        List<EffectInstance> effectsToGive = new ArrayList<>();

        if (lvl >= effects.length) lvl = effects.length - 1;
        String[] data = effects[lvl-1].split(" ");

        int i = 0;
        while (i < data.length) {
            try {
                ResourceLocation effect = new ResourceLocation(data[i]);
                Effect effectType = ForgeRegistries.POTIONS.getValue(effect);
                int level = Integer.parseInt(data[i+1]);
                effectsToGive.add(new EffectInstance(effectType, effectLength, level));
            } catch (Exception e){
                e.printStackTrace();
            }

            i += 2;
        }

        return effectsToGive;
    }
}

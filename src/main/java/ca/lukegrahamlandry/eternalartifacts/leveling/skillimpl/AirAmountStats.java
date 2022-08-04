package ca.lukegrahamlandry.eternalartifacts.leveling.skillimpl;

import ca.lukegrahamlandry.eternalartifacts.leveling.SkillStats;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AirAmountStats extends SkillStats {
    public int[] airAmountMultiplier;

    public float getAmount(int lvl) {
        if (lvl >= airAmountMultiplier.length) lvl = airAmountMultiplier.length - 1;
        return airAmountMultiplier[lvl-1];
    }
}

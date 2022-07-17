package ca.lukegrahamlandry.eternalartifacts.leveling;

import java.util.Map;

public class SkillStats {
    public int iconX, iconY;
    public String iconItem;
    public Map<String, Integer>[] upgradeItemCost;
    public Map<String, Integer>[] upgradeLevelCost;

    public int getMaxLevel() {
        return Math.min(upgradeLevelCost.length, upgradeItemCost.length);
    }
}

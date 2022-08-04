package ca.lukegrahamlandry.eternalartifacts.leveling;

import java.util.HashMap;
import java.util.Map;

public class SkillStats {
    public int iconX, iconY;
    public String iconTexture;
    public Map<String, Integer>[] upgradeItemCost;
    public Map<String, Integer>[] upgradeLevelCost;

    public int getMaxLevel() {
        return Math.max(upgradeLevelCost == null ? 0 : upgradeLevelCost.length, upgradeItemCost == null ? 0 : upgradeItemCost.length);
    }

    public Map<String, Integer> getItemUpgradeCost(int targetLevel) {
        if (upgradeItemCost == null || targetLevel > upgradeItemCost.length) return new HashMap<>();
        return upgradeItemCost[targetLevel-1];
    }

    public Map<String, Integer> getLevelUpgradeCost(int targetLevel) {
        if (upgradeLevelCost == null || targetLevel > upgradeLevelCost.length) return new HashMap<>();
        return upgradeLevelCost[targetLevel-1];
    }
}

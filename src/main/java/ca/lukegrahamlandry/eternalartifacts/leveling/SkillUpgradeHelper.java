package ca.lukegrahamlandry.eternalartifacts.leveling;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkillUpgradeHelper {
    public static void tryUnlock(PlayerEntity player, ResourceLocation artifact, ResourceLocation skill, int targetLevel) {
        ArtifactXpCapability.ifPresent(player, (xpData) -> {
            if (targetLevel != (xpData.getSkillLevel(artifact, skill) + 1)) {
                System.out.println("Can only upgrade to the next level.");
                return;
            }

            SkillStats skillData = SkillType.getSkill(skill).getStats();

            boolean affordItems = spendItems(player, skillData.getItemUpgradeCost(targetLevel), true);
            boolean affordXp = spendLevels(player, skillData.getLevelUpgradeCost(targetLevel), true);
            if (affordItems && affordXp && checkRequiredSkills(player, skillData.getUpgradeSkillRequirements(targetLevel), artifact)){
                spendItems(player, skillData.getItemUpgradeCost(targetLevel), false);
                spendLevels(player, skillData.getLevelUpgradeCost(targetLevel), false);
                xpData.learnSkill(artifact, skill, targetLevel);
                xpData.sync(player);
            }
        });
    }

    private static boolean checkRequiredSkills(PlayerEntity player, Map<String, Integer> upgradeSkillRequirements, ResourceLocation defaultArtifact) {
        AtomicBoolean success = new AtomicBoolean(true);

        ArtifactExperience artifactExperience = player.getCapability(ArtifactXpCapability.CAP).orElse(new ArtifactExperienceImpl());

        for (Map.Entry<String, Integer> cost : upgradeSkillRequirements.entrySet()){
            String[] costData = cost.getKey().split(" ");
            ResourceLocation artifactType = costData.length == 1 ? defaultArtifact : new ResourceLocation(costData[0]);
            ResourceLocation skillType = new ResourceLocation(costData[costData.length - 1]);
            int levelNeeded = cost.getValue();

            int currentLevel = artifactExperience.getSkillLevel(artifactType, skillType);
            if (currentLevel < levelNeeded) {
                success.set(false);
                break;
            }
        }

        return success.get();
    }

    // this already supports tags
    private static boolean spendItems(PlayerEntity player, Map<String, Integer> itemCost, boolean simulate) {
        for (String itemName : itemCost.keySet()){
            Item item = null;
            ITag.INamedTag<Item> tag = null;
            if (itemName.charAt(0) == '#'){
                tag = ItemTags.bind(itemName.substring(1));
            } else {
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            }

            int count = itemCost.get(itemName);
            for (int slot=0;slot<player.inventory.getContainerSize();slot++){
                ItemStack inSlot = player.inventory.getItem(slot);
                boolean matches = (item != null && inSlot.getItem() == item) || (tag != null && tag.contains(inSlot.getItem()));
                if (!matches) continue;
                if (inSlot.getCount() >= count) {
                    if (!simulate) inSlot.shrink(count);
                    count = 0;
                    if (!simulate) player.inventory.setItem(slot, inSlot);
                } else {
                    count -= inSlot.getCount();
                    if (!simulate) player.inventory.setItem(slot, ItemStack.EMPTY);
                }
            }
            if (count > 0) return false;
        }
        return true;
    }

    private static boolean spendLevels(PlayerEntity player, Map<String, Integer> xpCost, boolean simulate) {
        for (String xpType : xpCost.keySet()){
            int levels = xpCost.get(xpType);
            if (xpType.equals("vanilla")){
                if (player.experienceLevel < levels) return false;
                if (!simulate) player.giveExperienceLevels(-levels);
            } else {
                ResourceLocation xpKey = new ResourceLocation(xpType);
                AtomicBoolean success = new AtomicBoolean(true);
                ArtifactXpCapability.ifPresent(player, (xpData) -> {
                    int price = levels * xpData.getXpDisplayRatio(xpKey);
                    if (xpData.getExperience(xpKey) < price) {
                        success.set(false);
                        return;
                    }
                    if (!simulate) xpData.spendExperience(xpKey, price);
                });
                if (!success.get()) return false;
            }
        }
        return true;
    }
}

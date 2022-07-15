package ca.lukegrahamlandry.eternalartifacts.client.gui;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.registry.SkillType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FishingSkillsGui extends Screen {
    public FishingSkillsGui() {
        super(new StringTextComponent("Fishing Traits"));
    }

    @Override
    protected void init() {
        super.init();
        ArtifactXpCapability.ifPresent(Minecraft.getInstance().player, (xpData) -> {
            for (SkillType skill : SkillType.values()){
                int level = xpData.getSkillLevel(ArtifactExperience.FISHING, new ResourceLocation(ModMain.MOD_ID, skill.name().toLowerCase(Locale.ROOT)));
                TextComponent name = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.name().toLowerCase(Locale.ROOT));
                this.addButton(new ItemButton(skill.getStats().iconX, skill.getStats().iconY, (button) -> this.selectSkill(skill, level + 1), (p_238897_1_, p_238897_2_, p_238897_3_, p_238897_4_) -> {
                    this.renderTooltip(p_238897_2_, name, p_238897_3_, p_238897_4_);
                }, ForgeRegistries.ITEMS.getValue(new ResourceLocation(skill.getStats().iconItem)), String.valueOf(level)));
            }

            int currentLevel = (int) Math.floor(xpData.getExperience(ArtifactExperience.FISHING) / (float) ModMain.FISHING_XP_CONFIG.xpDisplayRatio());
            int totalLevel = (int) Math.floor(xpData.getTotalExperience(ArtifactExperience.FISHING) / (float) ModMain.FISHING_XP_CONFIG.xpDisplayRatio());
            this.levelText = new StringTextComponent("Your artifact is level " + totalLevel + ". You have " + currentLevel + " fishing levels to spend.");
        });

        this.doUpgrade = new Button(0, 120, 75, 20, new StringTextComponent("Upgrade"), this::doUpgradePress, (a, b, c, d) -> {});
        this.doUpgrade.active = false;
        this.addButton(this.doUpgrade);
    }

    private void doUpgradePress(Button button) {

    }

    Button doUpgrade;
    int upgradeTargetLevel = 0;
    SkillType upgradeSkill = null;
    List<Widget> itemCost = new ArrayList<>();
    IFormattableTextComponent upgradeText;
    IFormattableTextComponent levelText;


    private void selectSkill(SkillType skill, int targetLevel) {
        if (skill.getStats().upgradeItemCost == null || skill.getStats().upgradeItemCost.length <= targetLevel-1 || skill.getStats().upgradeLevelCost == null ||  skill.getStats().upgradeLevelCost.length <= targetLevel-1){
            System.out.println("Invalid serverconfig/eternalartifacts/fishing_artifact_stats.json. The key "  + skill.name().toLowerCase(Locale.ROOT) + " must have arrays upgradeItemCost and upgradeLevelCost with length greater than " + targetLevel);
            return;
        }
        this.upgradeTargetLevel = targetLevel;
        this.upgradeSkill = skill;

        for (Widget w : itemCost){
            w.visible = false;
        }
        itemCost.clear();

        int x = 80;
        boolean canAfford = true;
        for (Map.Entry<String, Integer> cost : skill.getStats().upgradeItemCost[targetLevel-1].entrySet()){
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cost.getKey()));
            int count = 0;
            PlayerInventory inv = Minecraft.getInstance().player.inventory;
            for (int i=0;i<inv.getContainerSize();i++){
                ItemStack stack = inv.getItem(i);
                if (stack.getItem() == item) count += stack.getCount();
            }
            if (count < cost.getValue()) canAfford = false;
            displayItem(x, 120, item, cost.getValue(), count >= cost.getValue(), new TranslationTextComponent(item.getDescriptionId()).append(new StringTextComponent(": " + count + "/" + cost.getValue())));
            x += 20;
        }

        x = 80;
        for (Map.Entry<String, Integer> cost : skill.getStats().upgradeLevelCost[targetLevel-1].entrySet()){
            Item item = Items.BARRIER;
            if (cost.getKey().equals("vanilla")){
                item = Items.EXPERIENCE_BOTTLE;
            } else if (cost.getKey().equals(ArtifactExperience.FISHING.toString())){
                item = Items.FISHING_ROD;
            }

            int levelCost = cost.getValue();
            AtomicInteger currentLevels = new AtomicInteger(0);

            if (cost.getKey().equals("vanilla")){
                currentLevels.set(Minecraft.getInstance().player.experienceLevel);
            } else {
                ArtifactXpCapability.ifPresent(Minecraft.getInstance().player, (xpData) -> {
                    currentLevels.set(xpData.getExperience(new ResourceLocation(cost.getKey())) / ModMain.FISHING_XP_CONFIG.xpDisplayRatio());
                });
            }

            if (currentLevels.get() < levelCost) canAfford = false;

            displayItem(x, 145, item, levelCost, currentLevels.get() >= levelCost, new StringTextComponent(cost.getKey() + " levels: " + currentLevels.get() + "/" + levelCost));
            x += 20;
        }

        upgradeText = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.name().toLowerCase(Locale.ROOT));
        this.doUpgrade.setMessage(new StringTextComponent(upgradeTargetLevel == 1 ? "Unlock" : "Upgrade to level " + targetLevel));
        this.doUpgrade.active = canAfford;
    }

    private void displayItem(int x, int y, Item item, int count, boolean valid, ITextComponent title){
        ItemButton w = new ItemButton(x, y, (b) -> {}, (p_238897_1_, p_238897_2_, p_238897_3_, p_238897_4_) -> {
            this.renderTooltip(p_238897_2_, title, p_238897_3_, p_238897_4_);
        }, item, String.valueOf(count));
        itemCost.add(w);
        w.active = valid;
        this.addButton(w);
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);

        if (this.upgradeSkill != null){
            FontRenderer fontrenderer = Minecraft.getInstance().font;
            drawString(matrixStack, fontrenderer, this.upgradeText, 0, 100, 0xFFFFFF00);
            drawString(matrixStack, fontrenderer, this.levelText, 0, 0, 0xFFFFFF00);
        }
    }
}

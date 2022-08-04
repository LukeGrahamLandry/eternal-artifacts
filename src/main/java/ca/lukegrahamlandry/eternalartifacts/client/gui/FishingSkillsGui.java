package ca.lukegrahamlandry.eternalartifacts.client.gui;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.leveling.SkillType;
import ca.lukegrahamlandry.eternalartifacts.leveling.SkillUpgradeHelper;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import ca.lukegrahamlandry.eternalartifacts.network.serverbound.UpgradeSkillPacket;
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
import java.util.concurrent.atomic.AtomicInteger;

public class FishingSkillsGui extends Screen {
    public FishingSkillsGui() {
        super(new StringTextComponent("Fishing Traits"));
    }

    int infoY = 100;

    @Override
    protected void init() {
        super.init();
        clearStuff();

        ArtifactXpCapability.ifPresent(Minecraft.getInstance().player, (xpData) -> {
            for (SkillType skill : SkillType.values()){
                int level = xpData.getSkillLevel(ArtifactExperience.FISHING, skill.getKey());
                TextComponent name = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.getKey().getPath());
                Widget b = new TextureButton(skill.getStats().iconX, skill.getStats().iconY, (button) -> this.selectSkill(skill, level + 1), (p_238897_1_, p_238897_2_, p_238897_3_, p_238897_4_) -> {
                    this.renderTooltip(p_238897_2_, name, p_238897_3_, p_238897_4_);
                }, new ResourceLocation(skill.getStats().iconTexture), String.valueOf(level));
                skillButtons.add(b);
                this.addButton(b);
            }

            int currentLevel = (int) Math.floor(xpData.getExperience(ArtifactExperience.FISHING) / (float) ModMain.FISHING_XP_CONFIG.xpDisplayRatio());
            int totalLevel = (int) Math.floor(xpData.getTotalExperience(ArtifactExperience.FISHING) / (float) ModMain.FISHING_XP_CONFIG.xpDisplayRatio());
            this.levelText = new TranslationTextComponent("gui.eternalartifacts.fishing", xpData.getArtifactLevel(ArtifactExperience.FISHING), currentLevel);
        });

        // todo skill dependencies. required: []. then have those skill icons show up below items/xp

        this.doUpgrade = new Button(0, infoY + 20, 150, 20, new StringTextComponent("Upgrade"), this::doUpgradePress, (a, b, c, d) -> {});
        this.doUpgrade.active = false;
        this.addButton(this.doUpgrade);
    }

    private void clearStuff() {
        upgradeSkill = null;
        upgradeTargetLevel = 0;
        for (Widget w : itemCost){
            w.visible = false;
        }
        itemCost.clear();
        for (Widget w : skillButtons){
            w.visible = false;
        }
        skillButtons.clear();
        upgradeText = null;
        if (doUpgrade != null) doUpgrade.visible = false;
    }

    private void doUpgradePress(Button button) {
        NetworkInit.INSTANCE.sendToServer(new UpgradeSkillPacket(ArtifactExperience.FISHING, this.upgradeSkill.getKey(), this.upgradeTargetLevel));
        SkillUpgradeHelper.tryUnlock(Minecraft.getInstance().player, ArtifactExperience.FISHING, upgradeSkill.getKey(), this.upgradeTargetLevel);
        init();
    }

    Button doUpgrade;
    int upgradeTargetLevel;
    SkillType upgradeSkill;
    List<Widget> skillButtons = new ArrayList<>();
    List<Widget> itemCost = new ArrayList<>();
    IFormattableTextComponent upgradeText;
    IFormattableTextComponent levelText;

    private void selectSkill(SkillType skill, int targetLevel) {
        if (skill.getStats().getMaxLevel() < targetLevel){
            this.upgradeSkill = skill;
            for (Widget w : itemCost){
                w.visible = false;
            }
            itemCost.clear();
            upgradeText = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.getKey().getPath());
            this.doUpgrade.setMessage(new StringTextComponent("Max Level"));
            this.doUpgrade.active = false;
            return;
        }

        this.upgradeTargetLevel = targetLevel;
        this.upgradeSkill = skill;

        for (Widget w : itemCost){
            w.visible = false;
        }
        itemCost.clear();

        int x = 160;
        boolean canAfford = true;
        for (Map.Entry<String, Integer> cost : skill.getStats().getItemUpgradeCost(targetLevel).entrySet()){
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cost.getKey()));
            int count = 0;
            PlayerInventory inv = Minecraft.getInstance().player.inventory;
            for (int i=0;i<inv.getContainerSize();i++){
                ItemStack stack = inv.getItem(i);
                if (stack.getItem() == item) count += stack.getCount();
            }
            if (count < cost.getValue()) canAfford = false;
            displayItem(x, infoY + 20, item, cost.getValue(), count >= cost.getValue(), new TranslationTextComponent(item.getDescriptionId()).append(new StringTextComponent(": " + count + "/" + cost.getValue())));
            x += 20;
        }

        x = 160;
        for (Map.Entry<String, Integer> cost : skill.getStats().getLevelUpgradeCost(targetLevel).entrySet()){
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
                    currentLevels.set(xpData.getExperience(new ResourceLocation(cost.getKey())) / xpData.getXpDisplayRatio(ArtifactExperience.FISHING));
                });
            }

            if (currentLevels.get() < levelCost) canAfford = false;

            displayItem(x, infoY + 45, item, levelCost, currentLevels.get() >= levelCost, new StringTextComponent(cost.getKey() + " levels: " + currentLevels.get() + "/" + levelCost));
            x += 20;
        }

        upgradeText = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.getKey().getPath());
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
            drawString(matrixStack, fontrenderer, this.upgradeText, 0, infoY, 0xFFFFFF00);
            drawString(matrixStack, fontrenderer, this.levelText, 0, 0, 0xFFFFFF00);
        }
    }
}

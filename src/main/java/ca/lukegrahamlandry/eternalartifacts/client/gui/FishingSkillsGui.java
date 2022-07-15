package ca.lukegrahamlandry.eternalartifacts.client.gui;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.registry.SkillType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class FishingSkillsGui extends Screen {
    public FishingSkillsGui() {
        super(new StringTextComponent("Fishing Traits"));
    }

    @Override
    protected void init() {
        super.init();
        for (SkillType skill : SkillType.values()){
            TextComponent name = new TranslationTextComponent("skill." + ModMain.MOD_ID + "." + skill.name().toLowerCase(Locale.ROOT));
            this.addButton(new ItemButton(skill.getStats().iconX, skill.getStats().iconY, (button) -> this.selectSkill(skill), (p_238897_1_, p_238897_2_, p_238897_3_, p_238897_4_) -> {
                this.renderTooltip(p_238897_2_, name, p_238897_3_, p_238897_4_);
            }, ForgeRegistries.ITEMS.getValue(new ResourceLocation(skill.getStats().iconItem))));
        }
    }

    private void selectSkill(SkillType skill) {
        System.out.println("cliecked " + skill.name());
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}

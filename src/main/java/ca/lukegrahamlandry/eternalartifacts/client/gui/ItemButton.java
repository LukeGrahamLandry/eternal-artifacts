package ca.lukegrahamlandry.eternalartifacts.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public class ItemButton extends Button {
    private final StringTextComponent cornerText;
    ItemStack display;
    public ItemButton(int x, int y, IPressable onPress, ITooltip tooltip, Item displayItem, String cornerText) {
        super(x, y, 20, 20, new StringTextComponent(""), onPress, tooltip);
        this.display = displayItem == null ? ItemStack.EMPTY : new ItemStack(displayItem);
        this.cornerText = new StringTextComponent(cornerText);
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(this.display, this.x + 2, this.y + 2);
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        int j = getFGColor();
        drawCenteredString(p_230431_1_, fontrenderer, this.cornerText, this.x + this.width, this.y + this.height - 5, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}

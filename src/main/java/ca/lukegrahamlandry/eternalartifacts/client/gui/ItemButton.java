package ca.lukegrahamlandry.eternalartifacts.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class ItemButton extends Button {
    ItemStack display;
    public ItemButton(int x, int y, IPressable onPress, ITooltip tooltip, Item displayItem) {
        super(x, y, 16, 16, new StringTextComponent(""), onPress, tooltip);
        this.display = displayItem == null ? ItemStack.EMPTY : new ItemStack(displayItem);
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(this.display, this.x, this.y);
    }
}

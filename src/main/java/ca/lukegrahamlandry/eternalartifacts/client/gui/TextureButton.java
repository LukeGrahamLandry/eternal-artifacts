package ca.lukegrahamlandry.eternalartifacts.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public class TextureButton extends Button {
    private final StringTextComponent cornerText;
    ResourceLocation texture;
    public TextureButton(int x, int y, IPressable onPress, ITooltip tooltip, ResourceLocation texture, String cornerText) {
        super(x, y, 20, 20, new StringTextComponent(""), onPress, tooltip);
        this.texture = texture;
        this.cornerText = new StringTextComponent(cornerText);
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int mouseX, int mouseY, float p_230431_4_) {
        super.renderButton(p_230431_1_, mouseX, mouseY, p_230431_4_);

        // p_230431_1_.pushPose();
        Minecraft.getInstance().getTextureManager().bind(this.texture);
        blit(p_230431_1_, this.x+2, this.y+2, this.getBlitOffset(), 0, 0, 16, 16, 16, 16);

        FontRenderer fontrenderer = Minecraft.getInstance().font;
        int j = getFGColor();
        drawCenteredString(p_230431_1_, fontrenderer, this.cornerText, this.x + this.width, this.y + this.height - 5, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        // p_230431_1_.popPose();
    }
}

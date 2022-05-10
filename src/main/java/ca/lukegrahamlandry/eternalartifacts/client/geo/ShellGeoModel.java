package ca.lukegrahamlandry.eternalartifacts.client.geo;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.tile.ShellTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.example.block.tile.FertilizerTileEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShellGeoModel extends AnimatedGeoModel<ShellTileEntity> {
    private static final ResourceLocation ANIMATION = new ResourceLocation(ModMain.MOD_ID, "animations/clam.animation.json");
    private static final ResourceLocation MODEL = new ResourceLocation(ModMain.MOD_ID, "geo/clam.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModMain.MOD_ID, "textures/block/clam.png");

    public ShellGeoModel() {
    }

    public ResourceLocation getAnimationFileLocation(ShellTileEntity animatable) {
        return ANIMATION;
    }

    public ResourceLocation getModelLocation(ShellTileEntity animatable) {
        return MODEL;
    }

    public ResourceLocation getTextureLocation(ShellTileEntity entity) {
        return TEXTURE;
    }
}

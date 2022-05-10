package ca.lukegrahamlandry.eternalartifacts.client.renderer;

import ca.lukegrahamlandry.eternalartifacts.client.geo.ShellGeoModel;
import ca.lukegrahamlandry.eternalartifacts.tile.ShellTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.example.client.model.tile.FertilizerModel;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ShellTileRenderer extends GeoBlockRenderer<ShellTileEntity> {
    public ShellTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new ShellGeoModel());
    }
}
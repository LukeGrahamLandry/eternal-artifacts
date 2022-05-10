package ca.lukegrahamlandry.eternalartifacts.registry;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.tile.ShellTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileTypeInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ModMain.MOD_ID);

    public static final RegistryObject<TileEntityType<ShellTileEntity>> SHELL = TILE_ENTITY_TYPES.register("shell",
            () -> TileEntityType.Builder.of(ShellTileEntity::new, BlockInit.SHELL.get()).build(null));
}

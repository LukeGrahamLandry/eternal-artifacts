package ca.lukegrahamlandry.eternalartifacts.mixin;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.ReloadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class ReloadCommandMixin {
    @Inject(at = @At("RETURN"), method = "reloadPacks")
    private static void onReload(Collection<String> p_241062_0_, CommandSource p_241062_1_, CallbackInfo ci) {
        ModMain.loadConfigs();
        ModMain.resyncConfigsToAll();
    }
}
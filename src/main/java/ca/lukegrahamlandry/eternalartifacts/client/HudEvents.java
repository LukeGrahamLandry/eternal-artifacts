package ca.lukegrahamlandry.eternalartifacts.client;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Abs;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HudEvents {
    public static Map<ResourceLocation, Integer> displayXp = new HashMap<>();
    public static Map<ResourceLocation, Integer> displayRatio = new HashMap<>();
    public static Map<ResourceLocation, Integer> xpToAdd = new HashMap<>();
    static int MAX_TIMER = 200;
    static int timer = MAX_TIMER;
    static Set<ResourceLocation> first = new HashSet<>();

    public static void onAddXp(ResourceLocation type, int ratio, int totalXp){
        displayRatio.put(type, ratio);
        xpToAdd.put(type, totalXp - displayXp.getOrDefault(type, 0));
        if (!displayXp.containsKey(type)) displayXp.put(type, 0);
        if (!first.contains(type)){
            first.add(type);
            xpToAdd.remove(type);
            displayXp.put(type, totalXp);
        }
        timer = MAX_TIMER;
    }

    @SubscribeEvent
    public static void drawXpHud(RenderGameOverlayEvent.Post event){
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;

        int yShift = -40;
        for (Map.Entry<ResourceLocation, Integer> data : xpToAdd.entrySet()){
            int nextXp = displayXp.getOrDefault(data.getKey(), 0) + 1;
            if (timer <= 0){
                displayXp.put(data.getKey(), nextXp);
                xpToAdd.put(data.getKey(), xpToAdd.get(data.getKey()) - 1);
                if (xpToAdd.get(data.getKey()) <= 0) xpToAdd.remove(data.getKey());
            }
            int ratio = displayRatio.get(data.getKey());
            float progress = ratio == 1 ? 0 : ((float) ((nextXp - 1) % ratio) / ratio) + ((1F / ratio) * (1 - ((float)timer) / MAX_TIMER));
            System.out.println(progress + " " + nextXp + " " + Math.floorDiv(nextXp, ratio) + " " + nextXp % ratio + " " + ratio);

            int level = Math.floorDiv(nextXp, ratio) - ((nextXp % ratio == 0) ? 1 : 0);
            renderBar(event.getMatrixStack(), data.getKey(), yShift, level, progress);
            yShift -= 25;
        }

        if (!xpToAdd.isEmpty()){
            if (timer <= 0){
                timer = MAX_TIMER;
            }
            timer--;
        }
    }

    public static void renderBar(MatrixStack matrix, ResourceLocation type, int yShift, int level, float progress){
        int xShift = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 91;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

        int j = 182;
        int k = (int)(progress * 183.0F);
        int l = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 32 + 3 + yShift;

        Minecraft.getInstance().gui.blit(matrix, xShift, l, 0, 64, 182, 5);
        if (k > 0) {
            Minecraft.getInstance().gui.blit(matrix, xShift, l, 0, 69, k, 5);
        }

        TranslationTextComponent s = new TranslationTextComponent("experience." + type.getNamespace() + "." + type.getPath(), level);

        int i1 = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - Minecraft.getInstance().font.width(s)) / 2;
        int j1 = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 31 - 4 + yShift;
        Minecraft.getInstance().font.draw(matrix, s, (float)(i1 + 1), (float)j1, 0);
        Minecraft.getInstance().font.draw(matrix, s, (float)(i1 - 1), (float)j1, 0);
        Minecraft.getInstance().font.draw(matrix, s, (float)i1, (float)(j1 + 1), 0);
        Minecraft.getInstance().font.draw(matrix, s, (float)i1, (float)(j1 - 1), 0);
        Minecraft.getInstance().font.draw(matrix, s, (float)i1, (float)j1, 8453920);
        
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

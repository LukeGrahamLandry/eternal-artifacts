package ca.lukegrahamlandry.eternalartifacts.network.clientbound;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.client.HudEvents;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.network.NetworkInit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

// server -> client
public class ExperienceUpdatePacket {
    private final ResourceLocation type;
    private final int xpAmount;
    private final int ratio;

    public ExperienceUpdatePacket(ResourceLocation type, int xpAmount, int ratio) {
        this.type = type;
        this.xpAmount = xpAmount;
        this.ratio = ratio;
    }

    public ExperienceUpdatePacket(PacketBuffer buf) {
        this(buf.readResourceLocation(), buf.readInt(), buf.readInt());
    }

    public static void encode(ExperienceUpdatePacket msg, PacketBuffer buf) {
        buf.writeResourceLocation(msg.type);
        buf.writeInt(msg.xpAmount);
        buf.writeInt(msg.ratio);
    }

    public static void handle(ExperienceUpdatePacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            HudEvents.onAddXp(msg.type, msg.ratio, msg.xpAmount);
            ArtifactXpCapability.ifPresent(Minecraft.getInstance().player, artifactExperience -> {
                artifactExperience.addExperience(ArtifactExperience.FISHING, msg.xpAmount);
            });
        });
        context.get().setPacketHandled(true);
    }
}
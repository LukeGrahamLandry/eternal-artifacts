package ca.lukegrahamlandry.eternalartifacts.network.serverbound;

import ca.lukegrahamlandry.eternalartifacts.client.HudEvents;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import ca.lukegrahamlandry.eternalartifacts.leveling.SkillUpgradeHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpgradeSkillPacket {
    private final ResourceLocation artifact;
    private final ResourceLocation skill;
    private final int targetLevel;

    public UpgradeSkillPacket(ResourceLocation artifact, ResourceLocation skill, int targetLevel) {
        this.artifact = artifact;
        this.skill = skill;
        this.targetLevel = targetLevel;
    }

    public UpgradeSkillPacket(PacketBuffer buf) {
        this(buf.readResourceLocation(), buf.readResourceLocation(), buf.readInt());
    }

    public static void encode(UpgradeSkillPacket msg, PacketBuffer buf) {
        buf.writeResourceLocation(msg.artifact);
        buf.writeResourceLocation(msg.skill);
        buf.writeInt(msg.targetLevel);
    }

    public static void handle(UpgradeSkillPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();
            SkillUpgradeHelper.tryUnlock(player, msg.artifact, msg.skill, msg.targetLevel);
        });
        context.get().setPacketHandled(true);
    }
}
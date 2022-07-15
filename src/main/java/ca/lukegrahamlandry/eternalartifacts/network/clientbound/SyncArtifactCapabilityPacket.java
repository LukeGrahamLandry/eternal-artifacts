package ca.lukegrahamlandry.eternalartifacts.network.clientbound;

import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactExperience;
import ca.lukegrahamlandry.eternalartifacts.leveling.ArtifactXpCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncArtifactCapabilityPacket {
    CompoundNBT data;

    public SyncArtifactCapabilityPacket(PacketBuffer buf) {
        this.data = buf.readAnySizeNbt();
    }

    public void encode(PacketBuffer buf){
        buf.writeNbt(this.data);
    }

    public SyncArtifactCapabilityPacket(ArtifactExperience cap){
        this.data = cap.write();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(this::handle);
        ctx.get().setPacketHandled(true);
    }

    private void handle() {
        if (Minecraft.getInstance().player == null) return;
        ArtifactXpCapability.ifPresent(Minecraft.getInstance().player, artifactExperience -> {
            artifactExperience.read(this.data);
        });
    }
}
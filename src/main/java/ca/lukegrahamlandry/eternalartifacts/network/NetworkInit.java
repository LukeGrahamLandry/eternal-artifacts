package ca.lukegrahamlandry.eternalartifacts.network;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.ExperienceUpdatePacket;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.SyncArtifactCapabilityPacket;
import ca.lukegrahamlandry.eternalartifacts.network.clientbound.SyncJsonConfigPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkInit {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerPackets() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModMain.MOD_ID, "packets"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), ExperienceUpdatePacket.class, ExperienceUpdatePacket::encode, ExperienceUpdatePacket::new, ExperienceUpdatePacket::handle);
        INSTANCE.registerMessage(nextID(), SyncJsonConfigPacket.class, SyncJsonConfigPacket::encode, SyncJsonConfigPacket::new, SyncJsonConfigPacket::handle);
        INSTANCE.registerMessage(nextID(), SyncArtifactCapabilityPacket.class, SyncArtifactCapabilityPacket::encode, SyncArtifactCapabilityPacket::new, SyncArtifactCapabilityPacket::handle);
    }
}
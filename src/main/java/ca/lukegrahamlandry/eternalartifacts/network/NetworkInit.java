package ca.lukegrahamlandry.eternalartifacts.network;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
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

        INSTANCE.registerMessage(nextID(), ExperienceUpdatePacket.class, ExperienceUpdatePacket::toBytes, ExperienceUpdatePacket::new, ExperienceUpdatePacket::handle);
    }
}
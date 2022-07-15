package ca.lukegrahamlandry.eternalartifacts.network.clientbound;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.client.HudEvents;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncJsonConfigPacket {
    private static final int MAX = 32767 * 2;
    JsonElement data;
    String config;

    public SyncJsonConfigPacket(PacketBuffer buf) {
        this.config = buf.readUtf(MAX);
        this.data = new JsonParser().parse(buf.readUtf(MAX)).getAsJsonObject();
    }

    public void encode(PacketBuffer buf){
        buf.writeUtf(config, MAX);
        buf.writeUtf(this.data.toString(), MAX);
    }

    public static SyncJsonConfigPacket fishingSkills(JsonElement data){
        return new SyncJsonConfigPacket("fishingskillstats", data);
    }

    public SyncJsonConfigPacket(String config, JsonElement data){
        this.config = config;
        this.data = data;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(this::handle);
        ctx.get().setPacketHandled(true);
    }

    private void handle() {
        if (config.equals("fishingskillstats")){
            ModMain.FISHING_SKILL_STATS.init((JsonObject) this.data);
        }
    }
}
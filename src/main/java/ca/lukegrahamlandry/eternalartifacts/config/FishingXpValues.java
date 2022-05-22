package ca.lukegrahamlandry.eternalartifacts.config;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class FishingXpValues {
    static JsonObject data;
    static Map<Item, Integer> itemXpAmounts;
    static Map<ITag.INamedTag<Item>, Integer> tagXpAmounts;
    static Integer xpRatio;

    public FishingXpValues(){
        init();
    }

    // call after item init
    public void init(){
        data = JsonConfig.load(ModMain.MOD_ID, "fishingxpvalues.json");
        itemXpAmounts = new HashMap<>();
        tagXpAmounts = new HashMap<>();

        if (!data.has("xpDisplayRatio")){
            ModMain.LOGGER.info("[FishingXpValues] missing key 'xpDisplayRatio' defaulting to 1");
        }
        xpRatio = data.has("xpDisplayRatio") ? data.get("xpDisplayRatio").getAsInt() : 1;

        JsonObject values = data.getAsJsonObject("values");
        for (Map.Entry<String, JsonElement> info : values.entrySet()){
            int amount = info.getValue().getAsInt();
            String key = info.getKey();
            if (key.charAt(0) == "#".charAt(0)){
                ITag.INamedTag<Item> tag = ItemTags.bind(key.substring(1));
                tagXpAmounts.put(tag, amount);
            } else {
                ResourceLocation rl = new ResourceLocation(key);
                if (ForgeRegistries.ITEMS.containsKey(rl)){
                    itemXpAmounts.put(ForgeRegistries.ITEMS.getValue(rl), amount);
                } else {
                    ModMain.LOGGER.info("[FishingXpValues] item {} is not registered", rl);
                }
            }
        }
    }

    public int xpDisplayRatio(){
        return xpRatio; // will crash if called to early
    }

    public int getXpValue(Item item){
        for (Map.Entry<Item, Integer> info : itemXpAmounts.entrySet()){
            if (info.getKey().equals(item)) return info.getValue();
        }

        for (Map.Entry<ITag.INamedTag<Item>, Integer> info : tagXpAmounts.entrySet()){
            if (info.getKey().contains(item)) return info.getValue();
        }

        return 0;
    }
}

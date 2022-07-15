package ca.lukegrahamlandry.eternalartifacts.config;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import ca.lukegrahamlandry.eternalartifacts.registry.SkillType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SkillsConfig {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    public JsonObject data;
    public Map<SkillType, SkillStats> skillStats;

    public SkillsConfig(){
        init();
    }

    public void init(){
        skillStats = new HashMap<>();
        init(JsonConfig.load(ModMain.MOD_ID, "fishing_artifact_stats.json"));
    }

    public void init(JsonObject data){
        this.data = data;
        for (SkillType skill : SkillType.values()){
            String key = skill.name().toLowerCase(Locale.ROOT);
            if (!data.has(key)){
                // would be good if i could try to load it from the jar incase like they updated the mod
                ModMain.LOGGER.error("serverconfig/eternalartifacts/fishing_artifact_stats.json missing key: " + key);
                continue;
            }

            try {
                SkillStats stats = GSON.fromJson(data.get(key), skill.clazz);
                skillStats.put(skill, stats);
            } catch (JsonSyntaxException e){
                e.printStackTrace();
                ModMain.LOGGER.error("serverconfig/eternalartifacts/fishing_artifact_stats.json failed to parse object: " + key);
            }
        }
    }

    public static class SkillStats {
        public int iconX, iconY;
        public String iconItem;
        public Map<String, Integer>[] upgradeItemCost;
        public Map<String, Integer>[] upgradeLevelCost;
    }
}

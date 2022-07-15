package ca.lukegrahamlandry.eternalartifacts.config;

import ca.lukegrahamlandry.eternalartifacts.ModMain;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

// TODO: use /reload command
public class JsonConfig {
    static FolderName SERVERCONFIG = new FolderName("serverconfig");

    // first check world/serverconfig/modid/filename
    // if not, copy from config/modid/filename to world/serverconfig/modid/filename
    // if not, copy from jar to config/modid/filename to world/serverconfig/modid/filename
    public static JsonObject load(String modid, String filename){
        Path worldConfig = ModMain.server.getWorldPath(SERVERCONFIG);
        Path modWorldConfig = worldConfig.resolve(modid);
        Path modDefaultConfig = FMLPaths.CONFIGDIR.get().resolve(modid);

        File worldConfigFile = new File(modWorldConfig.toFile(), filename);
        if (!worldConfigFile.exists()){
            File defaultConfigFile = new File(modDefaultConfig.toFile(), filename);

            if (!defaultConfigFile.exists()){
                copyDefaultFromJar(modid, filename);
            }

            modWorldConfig.toFile().mkdirs();
            copyFile(defaultConfigFile, worldConfigFile);

        }

        return loadData(worldConfigFile);
    }



    // jar -> config
    private static void copyDefaultFromJar(String modid, String filename) {
        // load the default json file from jar
        try {
            URI uri = JsonConfig.class.getResource("/config").toURI();
            AtomicReference<Path> myPath = new AtomicReference<>();
            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                myPath.set(fileSystem.getPath("/config"));
            } else if (uri.getScheme().equals("modjar")){
                // fixes java.nio.file.FileSystemNotFoundException: Provider modjar not installed
                FMLLoader.getLoadingModList().getModFiles().forEach((modFile) -> {
                    modFile.getMods().forEach((modInfo) -> {
                        if (modInfo.getModId().equals(ModMain.MOD_ID)){
                            myPath.set(modFile.getFile().findResource("config"));
                        }
                    });
                });
            }
            else {
                myPath.set(Paths.get(uri));
            }

            System.out.println("load default from jar: /config/" + filename);

            InputStream in = JsonConfig.class.getClassLoader().getResourceAsStream("/config/" + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            FMLPaths.CONFIGDIR.get().resolve(modid).toFile().mkdirs();
            File newFile = new File(FMLPaths.CONFIGDIR.get().resolve(modid).toFile(), filename);
            if (!newFile.exists()) newFile.createNewFile();
            FileWriter writer = new FileWriter(newFile);

            reader.lines().forEach(str -> {
                try {
                    writer.write(str + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    // config -> world/serverconfig
    private static void copyFile(File from, File newFile) {
        try {
            System.out.println("copying config file " + from.toString() + " to " + newFile.toString());

            InputStream in =Files.newInputStream(from.toPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            if (!newFile.exists()) newFile.createNewFile();
            FileWriter writer = new FileWriter(newFile);

            reader.lines().forEach(str -> {
                try {
                    writer.write(str + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject loadData(File statsDataFile) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(statsDataFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dataJson = String.join("\n", lines);
        JsonObject obj = new JsonParser().parse(dataJson).getAsJsonObject();
        ModMain.LOGGER.info("loaded config " + statsDataFile + "\n" + obj.toString());
        return obj;
    }
}
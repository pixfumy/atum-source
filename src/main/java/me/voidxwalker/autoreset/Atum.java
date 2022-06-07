package me.voidxwalker.autoreset;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Atum implements ModInitializer {
    private static final ModContainer mod = FabricLoader.getInstance().getModContainer("atum").orElseThrow(NullPointerException::new);
    public static String seed;
    public static boolean isRunning = false;
    public static Logger LOGGER = LogManager.getLogger();
    public static int difficulty = 1;

    public static void log(Level level, String message) {
        LOGGER.log(level,  message);
    }

    public static int getNextAttempt() {
        try {
            File file = new File("attempts.txt");
            int value;
            if (file.exists()) {
                Scanner fileReader = new Scanner(file);
                String string = fileReader.nextLine().trim();
                fileReader.close();
                try {
                    value = Integer.parseInt(string);
                } catch (NumberFormatException ignored) {
                    value = 0;
                }
            } else {
                value = 0;
            }
            value++;
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(Integer.toString(value));
            fileWriter.close();
            return value;
        } catch (IOException ignored) {
            return -1;
        }
    }
    public static Text getTranslation(String path, String text){
        if (Language.getInstance().get(path).equals(path)) {
            return  Text.of(text);
        } else {
            return Text.translatable(path);
        }
    }
    public static void saveDifficulty() {
        try {
            File file = new File("ardifficulty.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(difficulty));
            fileWriter.close();
        } catch (Exception exception) {
            log(Level.ERROR, "Could not save difficulty for Auto Reset:\n" + exception.getMessage());
        }
    }

    public static void loadDifficulty() {
        try {
            File file = new File("ardifficulty.txt");
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            scanner.close();
            difficulty = Integer.parseInt(line.trim());
            if(difficulty > 4){
                difficulty = 1;
            }
        } catch (Exception exception) {
            log(Level.ERROR, "Could not load difficulty for Auto Reset:\n" + exception.getMessage());
        }
    }
    public static void saveSeed() {
        try {
            File file = new File("seed.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(seed==null?"":seed);
            fileWriter.close();
        } catch (Exception exception) {
            log(Level.ERROR, "Could not save Seed for Auto Reset:\n" + exception.getMessage());
        }
    }
    public static void loadSeed() {
        try {
            File file = new File("seed.txt");
            Scanner scanner = new Scanner(file);
            if(scanner.hasNext()) {
                seed = scanner.nextLine();

            }
            else {
                seed="";
            }
            scanner.close();


        } catch (Exception exception) {
            log(Level.ERROR, "Could not load Seed for Auto Reset:\n" + exception.getMessage());
        }
    }
    @Override
    public void onInitialize() {
        if(!((Pingable)(new LevelLoadingScreen(null))).ping()){
            throw new IllegalStateException();
        }
        if(!((Pingable)(new DebugHud(MinecraftClient.getInstance()))).ping()){
            throw new IllegalStateException();
        }
        log(Level.INFO, "Initializing");
        File difficultyFile = new File("ardifficulty.txt");
        if (!difficultyFile.exists()) {
            saveDifficulty();
        } else {
            loadDifficulty();
        }
        File seedFile = new File("seed.txt");
        if (!seedFile.exists()) {
            saveSeed();
        } else {
            loadSeed();
        }
    }
}

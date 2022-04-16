package me.voidxwalker.autoreset;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Atum implements ModInitializer {
    public static String seed;
    public static boolean isRunning = false;
    public static Logger LOGGER = LogManager.getLogger();
    public static boolean loopPrevent2 = true;
    public static boolean isHardcore=false;

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
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

    public static void saveDifficulty() {
        try {
            File file = new File("arhardcore.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(isHardcore ? 1 : 0));
            fileWriter.close();
        } catch (Exception exception) {
            log(Level.ERROR, "Could not save difficulty for Auto Reset:\n" + exception.getMessage());
        }
    }

    public static void loadDifficulty() {
        try {
            File file = new File("arhardcore.txt");
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            scanner.close();
            isHardcore = Integer.parseInt(line.trim()) != 0;
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

       /* if(!((Pingable)(new DebugHud(MinecraftClient.getInstance()))).ping()){
            throw new IllegalStateException();
        }*/
        log(Level.INFO, "Initializing");
        File difficultyFile = new File("arhardcore.txt");
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

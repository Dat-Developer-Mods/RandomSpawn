package com.demmodders.randomspawn;

import com.demmodders.datmoddingapi.util.FileHelper;
import com.demmodders.randomspawn.structures.Player;
import com.google.gson.Gson;

import javax.annotation.Nullable;
import java.io.*;
import java.util.UUID;

public class Util {
    @Nullable
    public static Player getPlayer(UUID player){
        Gson gson = new Gson();
        File playerFile = new File(FileHelper.getConfigSubDir(RandomSpawn.MODID), player + ".json");
        try {
            Reader reader = new FileReader(playerFile);
            return gson.fromJson(reader, Player.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void savePlayer(UUID PlayerID, Player Player){
        Gson gson = new Gson();
        File playerFile = FileHelper.openFile(new File(FileHelper.getConfigSubDir(RandomSpawn.MODID), PlayerID + ".json"));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile));
            writer.write(gson.toJson(Player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

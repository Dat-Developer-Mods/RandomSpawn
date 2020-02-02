package com.demmodders.randomspawn;

import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.DatTeleporter;
import com.demmodders.datmoddingapi.util.FileHelper;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import com.demmodders.randomspawn.structures.Player;
import com.google.gson.Gson;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Util {

    /**
     * Get the player object containing their spawn and when they last teleported
     * @param player
     * @return
     */
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

    /**
     * Save the given player to a file
     * @param PlayerID The id of the player being saved
     * @param Player The Player object being saved
     */
    public static void savePlayer(UUID PlayerID, Player Player){
        Gson gson = new Gson();
        File playerFile = FileHelper.openFile(new File(FileHelper.getConfigSubDir(RandomSpawn.MODID), PlayerID + ".json"));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile));
            writer.write(gson.toJson(Player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a player with a random spawn
     * @return The player object
     */
    public static Player createPlayer(){
        return new Player(generateSpawnPos(RandomSpawnConfig.spawnDimension));
    }


    /**
     * Generates a random spawn position, trying to adjust so the player doesn't spawn in the ground
     * @param dimension The dimention to generate a spawn for
     * @return A block position which is probably safe to spawn in
     */
    public static BlockPos generateSpawnPos(int dimension){
        Random random = new Random();
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);

        int x = random.nextInt(RandomSpawnConfig.spawnDistance);
        if (random.nextBoolean()) x *= -1;
        int z = random.nextInt(RandomSpawnConfig.spawnDistance);
        if (random.nextBoolean()) z *= -1;


        BlockPos blockPos = world.getTopSolidOrLiquidBlock(new BlockPos(x + RandomSpawnConfig.spawnX  + .5D, 0, z + RandomSpawnConfig.spawnZ  + .5D));
        IBlockState state = world.getBlockState(blockPos);
        while ((state.getMaterial().isSolid() || state.getMaterial().isLiquid()) && !state.getBlock().isLeaves(state, world, blockPos) && blockPos.getY() < 255.0D){
            blockPos = blockPos.up();
            state = world.getBlockState(blockPos);
        }
        return blockPos.up();
    }

    public static void teleportPlayer(EntityPlayerMP player){
        Player playerObject = Util.getPlayer(player.getUniqueID());
        BlockPos spawnPos;
        if (playerObject == null || !RandomSpawnConfig.saveSpawn) {
            spawnPos = Util.generateSpawnPos(RandomSpawnConfig.spawnDimension);
            Util.savePlayer(player.getUniqueID(), new Player(spawnPos));
        } else {
            spawnPos = playerObject.spawn;
        }
        ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, true, "invulnerableDimensionChange", "field_184851_cj");
        if (player.dimension == RandomSpawnConfig.spawnDimension) {
            player.connection.setPlayerLocation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
        } else {
            player.changeDimension(RandomSpawnConfig.spawnDimension, new DatTeleporter(new Location(RandomSpawnConfig.spawnDimension, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0)));
        }
    }
}

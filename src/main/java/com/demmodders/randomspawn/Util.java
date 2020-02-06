package com.demmodders.randomspawn;

import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.BlockPosUtil;
import com.demmodders.datmoddingapi.util.DatTeleporter;
import com.demmodders.datmoddingapi.util.FileHelper;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import com.demmodders.randomspawn.structures.Player;
import com.google.gson.Gson;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.io.*;
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
        return new Player(RandomSpawnConfig.defaultSpawnDimension, generateSpawnPos(RandomSpawnConfig.defaultSpawnDimension));
    }


    /**
     * Generates a random spawn position, trying to adjust so the player doesn't spawn in the ground
     * @param dimension The dimention to generate a spawn for
     * @return A block position which is probably safe to spawn in
     */
    public static BlockPos generateSpawnPos(int dimension){
        BlockPos blockPos;
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);

        // Generate Position
        blockPos = null;
        while (blockPos == null || (!RandomSpawnConfig.waterSpawn && world.getBlockState(blockPos).getMaterial().isLiquid())){
            blockPos = generateCoordinates(world);
        }

        // Move position to up one where the player definitely isn't stuck inside a block
        IBlockState state = world.getBlockState(blockPos);
        while ((state.getMaterial().isSolid() || state.getMaterial().isLiquid()) && !state.getBlock().isLeaves(state, world, blockPos) && blockPos.getY() < 255.0D){
            blockPos = blockPos.up();
            state = world.getBlockState(blockPos);
        }

        // Return one block up
        return blockPos.add(.5D, 0D, .5D);
    }

    /**
     * Generates a set of random coordinates in the world and starts off finding a decent y coord
     * @param world The world to check
     * @return A block position almost in a decent place to spawn
     */
    public static BlockPos generateCoordinates(World world){
        Random random = new Random();

        int x = random.nextInt(RandomSpawnConfig.spawnDistance);
        if (random.nextBoolean()) x *= -1;
        int z = random.nextInt(RandomSpawnConfig.spawnDistance);
        if (random.nextBoolean()) z *= -1;

        return world.getTopSolidOrLiquidBlock(new BlockPos(x + RandomSpawnConfig.spawnX  + .5D, 0, z + RandomSpawnConfig.spawnZ  + .5D));
    }

    /**
     * Teleports the given player to their spawn
     * @param player The player being telported
     */
    public static void teleportPlayer(EntityPlayerMP player, boolean forceSpawn){
        Player playerObject = Util.getPlayer(player.getUniqueID());
        BlockPos spawnPos;
        World world;
        int dimension;

        // TODO: Account for world

        if (RandomSpawnConfig.forceSpawnDimension || forceSpawn || !FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(player.dimension).provider.canRespawnHere()) {
            dimension = RandomSpawnConfig.defaultSpawnDimension;
        } else {
            dimension = player.dimension;
        }
        world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);

        if (playerObject == null) {
            playerObject = new Player(dimension, generateSpawnPos(dimension));
            savePlayer(player.getUniqueID(), playerObject);
        } else if (!playerObject.spawn.containsKey(dimension) || !RandomSpawnConfig.saveSpawn){
            playerObject.spawn.put(dimension, generateCoordinates(world));
            savePlayer(player.getUniqueID(), playerObject);
        } else {
            spawnPos = BlockPosUtil.findSafeZ(dimension, playerObject.spawn.get(dimension), 4);
            if (spawnPos == null){
                playerObject.spawn.put(dimension, generateCoordinates(world));
            } else {
                playerObject.spawn.put(dimension, spawnPos);
            }
            savePlayer(player.getUniqueID(), playerObject);
        }

        spawnPos = playerObject.spawn.get(dimension);

        // Hack to prevent player moving wrongly
        ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, true, "invulnerableDimensionChange", "field_184851_cj");

        // Handle cross dimension
        if (player.dimension == dimension) {
            player.connection.setPlayerLocation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
        } else {
            player.changeDimension(dimension, new DatTeleporter(new Location(dimension, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0)));
        }

    }
}

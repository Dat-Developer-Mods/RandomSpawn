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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Random;
import java.util.UUID;

public class Util {
    public static final Logger LOGGER = LogManager.getLogger(RandomSpawn.MODID);
    /**
     * Get the player object containing their spawn and when they last teleported
     * @param player The UUID of the player you want
     * @return The player object
     */
    @Nullable
    public static Player getPlayer(UUID player){
        Gson gson = new Gson();
        File playerFile = new File(FileHelper.getWorldSubDir(RandomSpawn.MODID), player + ".json");
        try {
            Reader reader = new FileReader(playerFile);
            return gson.fromJson(reader, Player.class);
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
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
        File playerFile = FileHelper.openFile(new File(FileHelper.getWorldSubDir(RandomSpawn.MODID), PlayerID + ".json"));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile));
            writer.write(gson.toJson(Player));
            writer.close();
            LOGGER.debug(RandomSpawn.MODID + ": Saved player " + PlayerID);
        } catch (IOException e) {
            // e.printStackTrace();
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
        int genCount = RandomSpawnConfig.generationRetries;
        while ((blockPos == null || (!RandomSpawnConfig.waterSpawn && world.getBlockState(blockPos.down()).getMaterial().isLiquid()) || blockPos.getY() < 1) && (genCount != 0)) {
            if (RandomSpawnConfig.accurateGeneration) {
                BlockPos pos = generateCoordinates();
                BlockPos blockpos1;

                for (blockPos = new BlockPos(pos.getX(), 256, pos.getZ()); blockPos.getY() >= 0; blockPos = blockpos1)
                {
                    blockpos1 = blockPos.down();
                    IBlockState state = world.getBlockState(blockpos1);

                    if ((state.getMaterial().blocksMovement() && !state.getBlock().isLeaves(state, world, blockpos1) && !state.getBlock().isFoliage(world, blockpos1)) || state.getMaterial().isLiquid())
                    {
                        break;
                    }
                }
            } else {
                blockPos = world.getTopSolidOrLiquidBlock(generateCoordinates());
            }
            genCount -= 1;
        }

        if (genCount == 0) {
            blockPos = world.getTopSolidOrLiquidBlock(new BlockPos(RandomSpawnConfig.spawnX, 0, RandomSpawnConfig.spawnZ));
            LOGGER.warn(RandomSpawn.MODID + ": Failed to generate a spawn " + RandomSpawnConfig.generationRetries + " times in a row, this probably means the spawn radius is too small, or the radius contains only ocean and water spawning is disabled, or the radius contains only one giant hole. Whatever the problem is, someone is having a bad time while spawning");
        }

        // If the player is in water, move them to the top
        IBlockState state = world.getBlockState(blockPos);
        if (state.getMaterial().isLiquid()) {
            while (state.getMaterial().isLiquid() && blockPos.getY() < 255.0D) {
                blockPos = blockPos.up();
                state = world.getBlockState(blockPos);
            }
        }

        return blockPos;
    }

    /**
     * Generates a set of random coordinates in the world and starts off finding a decent y coord
     * @return A block position almost in a decent place to spawn
     */
    public static BlockPos generateCoordinates(){
        Random random = new Random();
        int x, z;

        x = random.nextInt(RandomSpawnConfig.spawnDistanceX);
        if (random.nextBoolean()) x *= -1;
        if (RandomSpawnConfig.splitRadius) z = random.nextInt(RandomSpawnConfig.spawnDistanceZ);
        else z = random.nextInt(RandomSpawnConfig.spawnDistanceX);
        if (random.nextBoolean()) z *= -1;

        return new BlockPos(x + RandomSpawnConfig.spawnX, 0, z + RandomSpawnConfig.spawnZ);
    }

    /**
     * Teleports the given player to their spawn
     * @param player The player being telported
     */
    public static void teleportPlayer(EntityPlayerMP player, boolean forceSpawn){
        Player playerObject = Util.getPlayer(player.getUniqueID());
        BlockPos spawnPos;
        int dimension;

        if (RandomSpawnConfig.forceSpawnDimension || forceSpawn || !FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(player.dimension).provider.canRespawnHere()) {
            dimension = RandomSpawnConfig.defaultSpawnDimension;
        } else {
            dimension = player.dimension;
        }

        if (playerObject == null) {
            playerObject = new Player(dimension, generateSpawnPos(dimension));
            savePlayer(player.getUniqueID(), playerObject);
        } else if (!playerObject.spawn.containsKey(dimension) || !RandomSpawnConfig.saveSpawn){
            playerObject.spawn.put(dimension, generateSpawnPos(dimension));
            savePlayer(player.getUniqueID(), playerObject);
        } else {
            if (RandomSpawnConfig.safeSpawn) {
                spawnPos = BlockPosUtil.findSafeZ(dimension, playerObject.spawn.get(dimension), 4);
                if (spawnPos == null) {
                    LOGGER.warn(RandomSpawn.MODID + ": Spawn is unsafe, giving the player a new spawn");
                    playerObject.spawn.put(dimension, generateSpawnPos(dimension));
                } else {
                    playerObject.spawn.put(dimension, spawnPos);
                }
                savePlayer(player.getUniqueID(), playerObject);
            }
        }



        spawnPos = playerObject.spawn.get(dimension);

        // Hack to prevent player moving wrongly
        ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, true, "invulnerableDimensionChange", "field_184851_cj");

        // Handle cross dimension
        if (player.dimension == dimension) {
            player.connection.setPlayerLocation(spawnPos.getX() + .5D, spawnPos.getY(), spawnPos.getZ() + .5D, 0, 0);
        } else {
            player.changeDimension(dimension, new DatTeleporter(new Location(dimension, spawnPos.getX() + .5D, spawnPos.getY(), spawnPos.getZ() + .5D, 0, 0)));
        }
    }
}

package com.demmodders.randomspawn.events;

import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import com.demmodders.randomspawn.structures.Player;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod.EventBusSubscriber(modid = RandomSpawn.MODID)
public class spawnWatcher {
    public static final Logger LOGGER = LogManager.getLogger(RandomSpawn.MODID);
    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
        LOGGER.info("Test");
        if (Util.getPlayer(e.player.getUniqueID()) == null){
            Util.teleportPlayer((EntityPlayerMP) e.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        Util.teleportPlayer((EntityPlayerMP) e.player);
    }
}

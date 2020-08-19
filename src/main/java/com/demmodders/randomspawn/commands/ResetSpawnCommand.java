package com.demmodders.randomspawn.commands;

import com.demmodders.datmoddingapi.util.DemConstants;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import com.demmodders.randomspawn.structures.Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ResetSpawnCommand extends CommandBase {
    @Override
    public String getName() {
        return "randomspawnreset";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("spawnreset");
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1){
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return (RandomSpawnConfig.saveSpawn ? DemConstants.TextColour.COMMAND + "/spawnreset [player] - " + DemConstants.TextColour.INFO + " Reset your spawn/the spawn of the given player to a new random location" : DemConstants.TextColour.INFO + "The server has disabled saving spawn, this will do nothing");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Ensure its either being called by a player, or on a player
        if (!(sender instanceof EntityPlayerMP) && args.length != 1) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "As the server, you can only reset the spawn of specific players, use: /spawnreset <player>"));
            return;
        }

        // If called by a player make sure they have permission
        if (sender instanceof EntityPlayerMP) {
            if (!PermissionAPI.hasPermission((EntityPlayerMP) sender, "datrandomteleport.rspawn.spawnreset") || (!PermissionAPI.hasPermission((EntityPlayerMP) sender, "datrandomteleport.rspawn.spawnresetother") && args.length > 0)) {
                sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You don't have permission to do that"));
                return;
            }
        }

        // Catch if has been called when saving spawn is disabled, as it means this command won't actually do anything
        if (!RandomSpawnConfig.saveSpawn) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "The server has disabled saving spawn points, this will do nothing"));
            return;
        }

        // Work out who the target is
        Player player;
        EntityPlayerMP target;
        if (args.length != 0) {
            target = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[0]);
        } else {
            target = (EntityPlayerMP) sender;
        }

        if (target != null){
            if(args.length != 0) sender.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "resetting " + args[0] + "'s spawn"));
        } else {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "Unable to find that player"));
            return;
        }

        // Figure out a new spawn point
        player = new Player(((EntityPlayerMP) sender).dimension, Util.generateSpawnPos(RandomSpawnConfig.defaultSpawnDimension));
        Util.savePlayer((target).getUniqueID(), player);
        target.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "Your spawn has been reset"));
    }
}

package com.demmodders.randomspawn.commands;

import com.demmodders.datmoddingapi.util.DemConstants;
import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.Util;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import com.demmodders.randomspawn.structures.Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetPlayerSpawnCommand extends CommandBase {
    @Override
    public String getName() {
        return "randomsetplayerspawn";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return DemConstants.TextColour.COMMAND + "/setplayerspawn [player] " + DemConstants.TextColour.INFO + " - Sets the spawn point of the given player";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You cannot set a player's spawn from the console"));
            return;
        } else if (!PermissionAPI.hasPermission((EntityPlayerMP) sender, "datrandomteleport.rspawn.admin")) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You don't have permission to do that"));
            return;
        }
        UUID targetPlayer;
        // Ensure its either being called by a player, or on a player
        if (args.length == 0) {
            targetPlayer = ((EntityPlayerMP) sender).getUniqueID();
        } else {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[0]);
            if (player != null) {
                targetPlayer = player.getUniqueID();
            } else {
                sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "Random spawn cannot find that player"));
                return;
            }
        }

        // Set the spawn in config and update the file
        Player thePlayer = Util.getPlayer(targetPlayer);
        if (thePlayer == null) {
            thePlayer = Util.createPlayer();
        }
        thePlayer.setDimSpawn(((EntityPlayerMP) sender).dimension, sender.getPosition());
        Util.savePlayer(targetPlayer, thePlayer);
        sender.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "Set " + (args.length == 0 ? "your spawn" : "the spawn of " + args[0]) + " to your current location: X=" + sender.getPosition().getX() + "Y=" + sender.getPosition().getY() + " Z=" + sender.getPosition().getZ()));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("setplayerspawn");
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}

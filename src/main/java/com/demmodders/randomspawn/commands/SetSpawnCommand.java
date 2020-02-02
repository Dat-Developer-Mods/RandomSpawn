package com.demmodders.randomspawn.commands;

import com.demmodders.randomspawn.RandomSpawn;
import com.demmodders.randomspawn.config.RandomSpawnConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends CommandBase {
    @Override
    public String getName() {
        return "randomsetspawn";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return TextFormatting.GOLD + "/setspawn - Sets the centre point where all players will spawn around";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Ensure its either being called by a player, or on a player
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "You must be a player to set the spawn, you could also set it in config"));
            return;
        }

        // Set the spawn in config and update the file
        RandomSpawnConfig.spawnX = (int) ((EntityPlayerMP) sender).posX;
        RandomSpawnConfig.spawnZ = (int) ((EntityPlayerMP) sender).posZ;
        ConfigManager.sync(RandomSpawn.MODID, Config.Type.INSTANCE);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("setspawn");
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}

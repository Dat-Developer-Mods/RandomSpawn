package com.demmodders.randomspawn.commands;

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
        return "Reset your spawn to a new random location /spawnreset [player]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP) && args.length != 1) return;
        if (sender instanceof EntityPlayerMP) {
            if (!PermissionAPI.hasPermission((EntityPlayerMP) sender, "datrandomteleport.rspawn.spawnreset") || (!PermissionAPI.hasPermission((EntityPlayerMP) sender, "datrandomteleport.rspawn.spawnresetother") && args.length > 0)) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permission to do that"));
                return;
            }
        }

        if (!RandomSpawnConfig.saveSpawn) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Spawn is not saved, this will do nothing"));
            return;
        }

        Player player;
        EntityPlayerMP target;
        if (args.length != 0) {
            target = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[0]);
        } else {
            target = (EntityPlayerMP) sender;
        }

        if (target != null){
            if(args.length != 0) sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "resetting " + args[0] + "'s spawn"));
        } else {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Unable to find that player"));
            return;
        }
        target.sendMessage(new TextComponentString(TextFormatting.GOLD + "Your spawn has been reset"));
        player = new Player(Util.generateSpawnPos(RandomSpawnConfig.spawnDimension));
        Util.savePlayer((target).getUniqueID(), player);
    }
}

package com.demmodders.randomspawn.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RespawnStorage implements Capability.IStorage<IRespawn> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRespawn> capability, IRespawn instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        BlockPos spawn = instance.getSpawn();
        tag.setLong("lastTeleport", instance.getLastTeleport());
        tag.setInteger("spawnX", spawn.getX());
        tag.setInteger("spawnY", spawn.getY());
        tag.setInteger("spawnZ", spawn.getZ());
        tag.setInteger("spawnDimension", instance.getSpawnDimension());
        return tag;
    }

    @Override
    public void readNBT(Capability<IRespawn> capability, IRespawn instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setLastTeleport(tag.getLong("lastTeleport"));
        instance.setSpawn(new BlockPos(tag.getInteger("spawnX"), tag.getInteger("spawnY"), tag.getInteger("spawnZ")));
        instance.setSpawnDimension(tag.getInteger("spawnDimension"));
    }
}

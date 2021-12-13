package com.supermartijn642.trashcans.packet;

import com.supermartijn642.trashcans.TrashCanTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 7/11/2020 by SuperMartijn642
 */
public abstract class TrashCanPacket {

    protected BlockPos pos;

    public TrashCanPacket(BlockPos pos){
        this.pos = pos;
    }

    public TrashCanPacket(PacketBuffer buffer){
        this.decodeBuffer(buffer);
    }

    public void encode(PacketBuffer buffer){
        buffer.writeBlockPos(this.pos);
    }

    protected void decodeBuffer(PacketBuffer buffer){
        this.pos = buffer.readBlockPos();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = contextSupplier.get().getSender();
        if(player == null || player.getCommandSenderBlockPosition().distSqr(this.pos) > 32 * 32)
            return;
        World world = player.level;
        if(world == null)
            return;
        TileEntity tile = world.getBlockEntity(this.pos);
        if(tile instanceof TrashCanTile)
            contextSupplier.get().enqueueWork(() -> this.handle(player, world, (TrashCanTile)tile));
    }

    protected abstract void handle(PlayerEntity player, World world, TrashCanTile tile);
}

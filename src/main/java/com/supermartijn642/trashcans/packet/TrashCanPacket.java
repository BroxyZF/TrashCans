package com.supermartijn642.trashcans.packet;

import com.supermartijn642.trashcans.TrashCanTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created 7/11/2020 by SuperMartijn642
 */
public abstract class TrashCanPacket<T extends TrashCanPacket> implements IMessage, IMessageHandler<T,IMessage> {

    protected BlockPos pos;

    public TrashCanPacket(BlockPos pos){
        this.pos = pos;
    }

    public TrashCanPacket(){
    }

    public void encode(ByteBuf buffer){
        buffer.writeInt(this.pos.getX());
        buffer.writeInt(this.pos.getY());
        buffer.writeInt(this.pos.getZ());
    }

    protected void decodeBuffer(ByteBuf buffer){
        this.pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    protected abstract void handle(T message, EntityPlayer player, World world, TrashCanTile tile);

    @Override
    public void fromBytes(ByteBuf buffer){
        this.decodeBuffer(buffer);
    }

    @Override
    public void toBytes(ByteBuf buffer){
        this.encode(buffer);
    }

    @Override
    public IMessage onMessage(T message, MessageContext ctx){
        EntityPlayerMP player = ctx.getServerHandler().player;
        if(player == null || player.getPosition().distanceSq(message.pos) > 32 * 32)
            return null;
        WorldServer world = player.getServerWorld();
        if(world == null)
            return null;
        TileEntity tile = world.getTileEntity(message.pos);
        if(tile instanceof TrashCanTile)
            world.addScheduledTask(() -> message.handle(message, player, world, (TrashCanTile)tile));

        return null;
    }
}

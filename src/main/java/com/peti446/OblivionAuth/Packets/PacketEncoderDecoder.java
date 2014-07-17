package com.peti446.OblivionAuth.Packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketEncoderDecoder extends AbstractPacket{
	byte reverse;

	public PacketEncoderDecoder(){
		this.reverse = 0;
	}

	public PacketEncoderDecoder(byte r){
		reverse = r;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeByte(reverse);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		reverse = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player){
		if(player != null){
			
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player){
	}

}

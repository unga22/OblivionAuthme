package com.peti446.OblivionAuth.Eventos;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sun.java2d.pipe.BufferedBufImgOps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.peti446.OblivionAuth.Herramientas;
import com.peti446.OblivionAuth.OblivionAuth;
import com.peti446.OblivionAuth.Packets.PacketEncoderDecoder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class AlEntrarJugador {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent EventPlayer){
		EntityPlayer player = EventPlayer.player;
		OblivionAuth.jugadorLogeandose.put(player, true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		String LoginOrRegister;
		if(Herramientas.TienePass(player)){
			//outputStream.writeUTF("Login");
			LoginOrRegister = "Login";
		} else {
			//outputStream.writeUTF("Register");
			LoginOrRegister = "Register";
		}
		
		EntityPlayerMP playermp = (EntityPlayerMP) player;
		FMLProxyPacket pkt = new FMLProxyPacket(buff, "OblivionAuth");
		OblivionAuth.Channel.sendTo(pkt, playermp);
		//OblivionAuth.packetPipeline.sendTo(new PacketEncoderDecoder(LoginOrRegister), playermp);
	}
	
	
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent EventPlayer){
		OblivionAuth.jugadorLogeandose.remove(EventPlayer.player);
	}
}

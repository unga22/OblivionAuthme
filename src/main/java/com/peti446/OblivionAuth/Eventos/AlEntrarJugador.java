package com.peti446.OblivionAuth.Eventos;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sun.java2d.pipe.BufferedBufImgOps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.peti446.OblivionAuth.Herramientas;
import com.peti446.OblivionAuth.OblivionAuth;
import com.peti446.OblivionAuth.GUI.GuiLogin;
import com.peti446.OblivionAuth.Packets.StringPacket;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class AlEntrarJugador {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent EventPlayer){
		EntityPlayer player = EventPlayer.player;
		OblivionAuth.jugadorLogeandose.put(player, true);
		String LoginOrRegister = "";
		if(OblivionAuth.UsarMysql){
			if(Herramientas.MysqlTienePass(player)){
				LoginOrRegister = "Login";
			} else {
				LoginOrRegister = "Register";
			}
		} else {
			if(Herramientas.TienePass(player)){
				LoginOrRegister = "Login";
			} else {
				LoginOrRegister = "Register";
			}
		}
		EntityPlayerMP playermp = (EntityPlayerMP) player;
		OblivionAuth.Channel.sendTo(new StringPacket(LoginOrRegister), playermp);
	}
	
	
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent EventPlayer){
		OblivionAuth.jugadorLogeandose.remove(EventPlayer.player);
	}
}

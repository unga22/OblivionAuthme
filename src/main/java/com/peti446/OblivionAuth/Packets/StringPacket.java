package com.peti446.OblivionAuth.Packets;

import scala.util.control.Exception.Catch;

import com.peti446.OblivionAuth.Herramientas;
import com.peti446.OblivionAuth.OblivionAuth;
import com.peti446.OblivionAuth.GUI.GuiLogin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class StringPacket implements IPacket{
	String mensage;
	public StringPacket(){
		mensage = "";
	}
	public StringPacket(String text){
		mensage = text;
	}
    @Override
    public void readBytes(ByteBuf bytes) {
        mensage = ByteBufUtils.readUTF8String(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        ByteBufUtils.writeUTF8String(bytes, mensage);
        
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(NetHandlerPlayClient player) {
        	GuiLogin gl = new GuiLogin(mensage);
        	Minecraft.getMinecraft().displayGuiScreen(gl);
    }

    @Override
    public void handleServerSide(NetHandlerPlayServer player){
    	if(OblivionAuth.UsarMysql){
    		if(Herramientas.MysqlTienePass(player.playerEntity)){
    			if(Herramientas.MysqlComprobarPass(player.playerEntity, mensage)){
    				OblivionAuth.jugadorLogeandose.remove(player.playerEntity);
        			OblivionAuth.jugadorLogeandose.put(player.playerEntity, false);
        			player.playerEntity.addChatMessage(new ChatComponentText("You are logged in !"));
    			} else {
    				player.kickPlayerFromServer("Bad password");
    			}
    		} else {
    			Herramientas.MysqlGuardarPassDelJugador(player.playerEntity, mensage);
        		OblivionAuth.jugadorLogeandose.remove(player.playerEntity);
    			OblivionAuth.jugadorLogeandose.put(player.playerEntity, false);
    			player.playerEntity.addChatMessage(new ChatComponentText("You are registred !"));
    		}
    	} else {
        	if(Herramientas.TienePass(player.playerEntity)){
        		if(Herramientas.CompbobarPass(player.playerEntity, mensage)){
        			OblivionAuth.jugadorLogeandose.remove(player.playerEntity);
        			OblivionAuth.jugadorLogeandose.put(player.playerEntity, false);
        			player.playerEntity.addChatMessage(new ChatComponentText("You are logged in !"));
        		} else {
        			player.kickPlayerFromServer("Bad password");
        		}
        	} else {
        		Herramientas.GuardarPassDelJugador(player.playerEntity, mensage);
        		OblivionAuth.jugadorLogeandose.remove(player.playerEntity);
    			OblivionAuth.jugadorLogeandose.put(player.playerEntity, false);
    			player.playerEntity.addChatMessage(new ChatComponentText("You are registred !"));
        	}
    	}
    }
}

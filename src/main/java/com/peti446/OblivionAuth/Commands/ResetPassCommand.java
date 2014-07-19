package com.peti446.OblivionAuth.Commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.peti446.OblivionAuth.Herramientas;
import com.peti446.OblivionAuth.OblivionAuth;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class ResetPassCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "authreset";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "/authreset <player>";
	}

	@Override
	public List getCommandAliases(){
		return Arrays.asList("ar", "authr");
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(sender instanceof EntityPlayer){
			EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName());
			if(args.length < 1){
				player.addChatMessage(new ChatComponentText("§4[ForgeAuth] Use: /authreset <user>"));
			} else {
				if(OblivionAuth.UsarMysql){
					EntityPlayerMP diana = MinecraftServer.getServer().getConfigurationManager().func_152612_a(args[0]);
					if(diana != null){
						if(Herramientas.MysqlTienePass(diana)){
							Herramientas.MysqlBorrarUsuario(diana);
							player.addChatMessage(new ChatComponentText("§4[ForgeAuth] " + args[0] + "'s password reset."));
						} else {
							player.addChatMessage(new ChatComponentText("§4[ForgeAuth] " + "This player " + args[0] + " doesn't exists."));
						}
					} else {
						player.addChatMessage(new ChatComponentText("§4[ForgeAuth] " + "This player " + args[0] + " doesn't exists."));
					}
				} else {
					File userFile = new File(OblivionAuth.userFolder, args[0]);
					if(userFile.exists()){
						userFile.delete();
						player.addChatMessage(new ChatComponentText("§4[ForgeAuth] " + args[0] + "'s password reset."));
					} else {
						player.addChatMessage(new ChatComponentText("§4[ForgeAuth] " + "This player " + args[0] + " doesn't exists."));
					}
				}
			}
		}
	}
	
	@Override
	public int getRequiredPermissionLevel(){
		return 4;
	}

}

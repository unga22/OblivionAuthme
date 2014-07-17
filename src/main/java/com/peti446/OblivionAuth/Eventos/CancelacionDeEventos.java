package com.peti446.OblivionAuth.Eventos;

import com.peti446.OblivionAuth.OblivionAuth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CancelacionDeEventos{
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Eventos que Cancelar mientras un jugador se logea
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent e) {
		Cancelar(e);
	}
	@SubscribeEvent
	public void playerHurt(LivingHurtEvent e) {
		Cancelar(e);
	}

	@SubscribeEvent
	public void playerAttack(AttackEntityEvent e) {
		Cancelar(e);
	}

	@SubscribeEvent
	public void playerInteractEntity(EntityInteractEvent e) {
		Cancelar(e);
	}
	
	@SubscribeEvent
	public void minecartInterract(MinecartCollisionEvent e) {
		Cancelar (e);
	}

	@SubscribeEvent
	public void playerFillBucket(FillBucketEvent e) {
		Cancelar(e);
	}

	@SubscribeEvent
	public void playerItemPickup(EntityItemPickupEvent e) {
		Cancelar(e);
	}
	@SubscribeEvent
	public void playerChat(ServerChatEvent e) {
		if(OblivionAuth.jugadorLogeandose.get(e.player) == true){
			e.setCanceled(true);
		}
	}
	@SubscribeEvent
	public void playerCmd(CommandEvent e) {
		if(e.sender instanceof EntityPlayer){
			if(OblivionAuth.jugadorLogeandose.get((EntityPlayer) e.sender) == true){
				e.setCanceled(true);
			}
		}
	}
    //Eventos que Cancelar mientras un jugador se logea
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Metodo general para cancelar los eventos en general para no repetir muchas vezes el if que me canso XD
	private void Cancelar(EntityEvent e) {
		if (e.entity instanceof EntityPlayer && OblivionAuth.jugadorLogeandose.get(e.entity) == true){
			e.setCanceled(true);
		}
	}
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Metodo general para cancelar los eventos en general para no repetir muchas vezes el if que me canso XD
}

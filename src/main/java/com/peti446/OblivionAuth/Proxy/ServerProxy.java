package com.peti446.OblivionAuth.Proxy;

import com.peti446.OblivionAuth.OblivionAuth;
import com.peti446.OblivionAuth.Eventos.AlEntrarJugador;
import com.peti446.OblivionAuth.Eventos.CancelacionDeEventos;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy {
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Registrar cosas para el Server
    public void registerRenderers() {
    	MinecraftForge.EVENT_BUS.register(new CancelacionDeEventos());
    	FMLCommonHandler.instance().bus().register(new AlEntrarJugador());
    }
    //Registrar cosas para el Server
    //----------------------------------------------------------------------------------------------------------------------------------------------------
}

package com.peti446.OblivionAuth.Proxy;

import com.peti446.OblivionAuth.ClientPacketHandler;
import com.peti446.OblivionAuth.OblivionAuth;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends ServerProxy{
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Registrar cosas para el Server
	@Override
    public void registerRenderers() {
		OblivionAuth.Channel.register(new ClientPacketHandler());
    }
    //Registrar cosas para el Server
    //----------------------------------------------------------------------------------------------------------------------------------------------------
}

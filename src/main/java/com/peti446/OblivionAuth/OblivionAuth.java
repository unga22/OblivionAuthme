package com.peti446.OblivionAuth;

import java.io.File;
import java.util.HashMap;

import com.peti446.OblivionAuth.Commands.ResetPassCommand;
import com.peti446.OblivionAuth.Mysql.Mysql;
import com.peti446.OblivionAuth.Packets.PacketPipeline;
import com.peti446.OblivionAuth.Proxy.ClientProxy;
import com.peti446.OblivionAuth.Proxy.ServerProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = OblivionAuth.MODID, version = OblivionAuth.VERSION, name = OblivionAuth.name)
public class OblivionAuth{
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Datos del Mod
    public static final String MODID = "oblivionauth";
    public static final String name = "OblivionAuth";
    public static final String VERSION = "1.0";
    //Datos del Mod
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Instancia
    @Instance(MODID)
    public static OblivionAuth instance;
    //Instancia
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Declaracion de donde estan los proxys
    @SidedProxy(clientSide = "com.peti446.OblivionAuth.Proxy.ClientProxy", serverSide = "com.peti446.OblivionAuth.Proxy.ServerProxy")
    public static ServerProxy serverProxy;
    //Declaracion de donde estan los proxys
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Variables
    
    public static FMLEventChannel Channel;
    
    public static File userFolder;
    public static boolean UsarMysql;
    public static Configuration config;
    public static HashMap<String, String> mysqlDatos = new HashMap<String, String>();
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    public static Mysql mysql;
    public static HashMap<EntityPlayer, Boolean> jugadorLogeandose = new HashMap<EntityPlayer, Boolean>();  
    //Variables
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //PreInit
    @EventHandler
    public void PreInit(FMLPreInitializationEvent preEvent){
    	//Se Obtiene la direcion de la carpeta de los jugadores
    	userFolder = new File(preEvent.getSuggestedConfigurationFile().getParentFile(), "OblivionAuthPlayers");
    	//Combrobar si se esta ejecutando desde un servidor
    	if(preEvent.getSide() == Side.SERVER){
    		//Crear la carpeta userFolder si no existe
    		if(!userFolder.exists()){
        		userFolder.mkdir();
    		}
    		//Se genera la config si no existe y se carga la config
    		config = new Configuration(preEvent.getSuggestedConfigurationFile());
    		config.load();
    	}
    }
    //PreInit
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Init
	@EventHandler
	public void Init(FMLInitializationEvent event){
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("OblivionAutt");
		//Registra el ServerProxy
		serverProxy.registerRenderers();
		//Registracion de PacketPipeline
		packetPipeline.initialise("OblivionAuth");
		//Combrobar si se esta ejecutando desde un servidor
		if(event.getSide() == Side.SERVER){
			//Obtener de la config los datos
			UsarMysql = config.getBoolean("UseMysql", Configuration.CATEGORY_GENERAL, false, "Set this to true if you want to use mysql, if you let it in false all the pass from user will be saved in files!");
			mysqlDatos.put("host", config.getString("MYSQL-Host", Configuration.CATEGORY_GENERAL, "localhost", "This is the addres to you DB you only need to change the mysql setings if you set UseMysql = true"));
			mysqlDatos.put("user", config.getString("MYSQL-User", Configuration.CATEGORY_GENERAL, "username", "This is the username from you DB you only need to change the mysql setings if you set UseMysql = true"));
			mysqlDatos.put("pass", config.getString("MYSQL-Password", Configuration.CATEGORY_GENERAL, "password", "This is the password from you DB you only need to change the mysql setings if you set UseMysql = true"));
			mysqlDatos.put("port", config.getString("MYSQL-Port", Configuration.CATEGORY_GENERAL, "3306", "This is the port from you DB you only need to change the mysql setings if you set UseMysql = true"));
			mysqlDatos.put("database", config.getString("MYSQL-Database", Configuration.CATEGORY_GENERAL, "Minecraft", "This is the name from the Database you only need to change the mysql setings if you set UseMysql = true"));
			//Si se usa mysql iniciar la mysql y borrar la carpeta de usuarios en el server
			if(UsarMysql == true){
				try{			
					mysql = new Mysql();
				} catch (Exception e){
					e.printStackTrace();
				}
				if(userFolder.exists()){
					userFolder.delete();
				}
			}
		}
	}
    //Init
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //PostInit
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		//Registracion Secundaria de Pipeline
		packetPipeline.postInitialise();
		//Combrobar si se esta ejecutando desde un servidor
		if(postEvent.getSide() == Side.SERVER){
			//Si la config ha cambiado guardarla
			if(config.hasChanged()){
				config.save();
			}
		}
	}
    //PostInit
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //OnServerStart
	@EventHandler
	public void serverStart(FMLServerStartingEvent serverStartEvent){
		//Combrobar si se esta ejecutando desde un servidor
		if(serverStartEvent.getSide() == Side.SERVER){
			//Registracion de Comandos
			serverStartEvent.registerServerCommand(new ResetPassCommand());
		}
	}
    //OnServerStart
    //----------------------------------------------------------------------------------------------------------------------------------------------------
}

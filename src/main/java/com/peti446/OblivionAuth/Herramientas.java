package com.peti446.OblivionAuth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.codec.binary.Hex;

import com.peti446.OblivionAuth.Mysql.Mysql;

import net.minecraft.entity.player.EntityPlayer;

public class Herramientas {
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//Herramientas Generales
    
	//----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta general para leer un archivos
	public static String leerArchivo(File archivo){
		try{
			InputStream imputsteam = new FileInputStream(archivo);
			InputStreamReader imputsteamreader = new InputStreamReader(imputsteam);
			BufferedReader bufferedreader = new BufferedReader(imputsteamreader);
			String linea = bufferedreader.readLine();
			bufferedreader.close();
			return linea;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
    //Herramienta para guardar la contraseña en el archivo
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta general para leer un archivos
	public static boolean GuardarArchivo(File archivo, String pass){
		try{
			if(archivo.exists()){
				archivo.delete();
			}
			archivo.createNewFile();
			FileWriter escritordearchivos = new FileWriter(archivo);
			escritordearchivos.write(pass);
			escritordearchivos.close();
			return true;
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
    //Herramienta general para leer un archivos
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para leer el archivo del jugador selecionado
	public static String LeerArchivoDelJugador(String player){
		return leerArchivo(new File(OblivionAuth.userFolder , player));
	}
    //Herramienta para leer el archivo del jugador selecionado
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para guardar la contraseña en el archivo
	public static boolean GuardarPassDelJugador(EntityPlayer player, String pass){
		return GuardarArchivo(new File(OblivionAuth.userFolder, player.getDisplayName()), pass); 
	}
    //Herramienta para guardar la contraseña en el archivo
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	
//Herramientas Generales
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Herramientas especificas
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para comprobar si tiene contraseña
	public static boolean TienePass(EntityPlayer player){
		String nombreUsuario = player.getDisplayName();
		File ArchivoDelJugador = new File(OblivionAuth.userFolder, nombreUsuario);
		if(ArchivoDelJugador.exists()){
			if(LeerArchivoDelJugador(nombreUsuario).isEmpty()){
				return false;
			}
			return true;
		}
		return false;
	}
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para comprobar si la pass es correcta
	public static boolean CompbobarPass(EntityPlayer player, String pass){
		String nombreUsuario = player.getDisplayName();
		File ArchivoDelJugador = new File(OblivionAuth.userFolder, nombreUsuario);
		if(ArchivoDelJugador.exists()){
			String PassGuardadaEnArchivo = LeerArchivoDelJugador(nombreUsuario);
			if(PassGuardadaEnArchivo.equals(pass)){
				return true;
			}
		}
		return false;
	}
	
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para codificar la pass
	public static String CodificarPass(String pass){
		MessageDigest md;
		try{
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(pass.getBytes());
			return new String(Hex.encodeHex(md.digest()));
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}
	
//Herramientas especificas
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Herramientas Mysql
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    //Herramienta para obtener sql
	public static Mysql obtenerMysql(){
		return OblivionAuth.mysql;
	}
	
    //Herramienta Crear tabla si no existe (Mysql)
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	public static void MysqlCrearTablaSiNoExiste(){
		Mysql sql = obtenerMysql();
		sql.actualizarQuery("CREATE TABLE IF NOT EXISTS OblivionAuthPlayers(Id INT AUTO_INCREMENT PRIMARY KEY, Player VARCHAR(30), Password TEXT(1000000));");
	}
    //Herramienta para guardar la contraseña en la mysql (Mysql)
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	public static void MysqlGuardarPassDelJugador(EntityPlayer player, String pass){
		Mysql sql = obtenerMysql();
		sql.actualizarQuery("INSERT INTO OblivionAuthPlayers(Player, Password) VALUES ('" + player.getDisplayName() + "', '" + pass + "');");
	}

    //Herramienta para comprobar si tiene contraseña (Mysql)
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean MysqlTienePass(EntityPlayer player){
		Mysql sql = obtenerMysql();
		Connection conn = sql.obtenerConexion();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM OblivionAuthPlayers WHERE Player=?");
			ps.setString(1, player.getDisplayName());
			rs = ps.executeQuery();
			rs.last();
			if(rs.getRow() != 0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql.cerrarRecursos(rs, ps);
		}
		return false;
	}
	
    //Herramienta para comprobar si la pass es correcta
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	public static boolean MysqlComprobarPass(EntityPlayer player, String pass){
		Mysql sql = obtenerMysql();
		Connection conn = sql.obtenerConexion();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String passEnMysql = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM OblivionAuthPlayers WHERE Player=?");
			ps.setString(1, player.getDisplayName());
			rs = ps.executeQuery();
			rs.last();
			if(rs.getRow() != 0){
				rs.first();
				passEnMysql = rs.getString("Password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql.cerrarRecursos(rs, ps);
		}
		if (passEnMysql.equals(pass)){
			return true;
		}
		return false;
	}
	
    //Herramienta para comprobar si la pass es correcta
    //----------------------------------------------------------------------------------------------------------------------------------------------------
	public static void MysqlBorrarUsuario(EntityPlayer player){
		Mysql sql = obtenerMysql();
		sql.actualizarQuery("DELETE FROM OblivionAuthPlayers WHERE Player='" + player.getDisplayName() + "'");
	}
	
//Herramientas Mysql
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}

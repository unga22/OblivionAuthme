package com.peti446.OblivionAuth.Mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.peti446.OblivionAuth.OblivionAuth;

public class Mysql {
	 private String url;
	 private String Driver;
	 private String DB;
	 private String user;
	 private String pass;
	 private Connection connection;
	public Mysql(){
		url = "jdbc:mysql://" + OblivionAuth.mysqlDatos.get("host") + ":" + OblivionAuth.mysqlDatos.get("port") +"/";
		Driver = "com.mysql.jdbc.Driver";
		DB = OblivionAuth.mysqlDatos.get("database");
		user =  OblivionAuth.mysqlDatos.get("user");
		pass = OblivionAuth.mysqlDatos.get("pass");
		abrirConexion();
	}
	
	public Connection abrirConexion(){
		try {
			Class.forName(Driver);
			Connection conn = DriverManager.getConnection(url + DB, user, pass);
			connection = conn;
			return conn;
		} catch (SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Connection obtenerConexion(){
		return connection;
	}
	
	public boolean tieneConexion(){
		try {
			return connection != null || connection.isValid(1);
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void actualizarQuery(String query){
		Connection conn = connection;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrarRecursos(null, st);
		}
	}
	
	public void cerrarRecursos(ResultSet rs, PreparedStatement ps){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void cerrarConexion(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection = null;
		}
	}
}

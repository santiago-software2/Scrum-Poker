/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SUPERTRONICA
 */
public class ConexionBDD {

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            // Manera de Conexion a la Base de Datos
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Parametros de conexion url/usuario/clave en mysql
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/scrum_poker?autoReconnect=true&useSSL=false&serverTimezone=UTC",
                    "root",
                    "software_santiago1"
            );
            System.out.println("CONECTADO");
        } catch (ClassNotFoundException | java.sql.SQLException e) { // CAPTURAR ERRORES
            System.out.println("ERROR DE CONEXION A LA BASE DE DATOS");
            e.printStackTrace();
        }
        return conexion;
    }
}

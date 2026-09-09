/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.ConexionBDD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public abstract class Usuario {

    protected int id;
    protected String nombre;
    protected String email;
    protected String telefono;
    protected String usuario;
    protected String contrasena;
    protected String rol;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String telefono, String usuario, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método para insertar usuario compartido por las subclases
    public int insertarUsuario() {
        int idGenerado = -1;
        String sentenciaSQL = "{call sp_crear_usuario(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conectado = new ConexionBDD().conectar(); CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setString(1, nombre);
            ejecutar.setString(2, email);
            ejecutar.setString(3, telefono);
            ejecutar.setString(4, usuario);
            ejecutar.setString(5, contrasena);
            ejecutar.setString(6, rol);
            ejecutar.registerOutParameter(7, Types.INTEGER);

            ejecutar.execute();
            idGenerado = ejecutar.getInt(7);

            if (idGenerado > -1) {
                System.out.println("Usuario creado con éxito en la BDD");
            } else {
                System.out.println("El usuario no se pudo crear.");
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }

        return idGenerado;
    }

    // Métodos abstractos que implementarán las clases hijas si lo requieren
    public abstract ArrayList<String[]> obtenerUsuarios();

    public abstract boolean actualizarUsuario();

    public abstract boolean inhabilitarUsuario();

    
}

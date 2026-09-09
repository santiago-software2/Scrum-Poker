/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.ConexionBDD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public abstract class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String username;
    private String contraseña;
    private String rol;
    private String estado;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String telefono, String username, String contraseña, String rol, String estado, PreparedStatement ejecutar, ResultSet resultado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.username = username;
        this.contraseña = contraseña;
        this.rol = rol;
        this.estado = estado;
        this.ejecutar = ejecutar;
        this.resultado = resultado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsername() {
        return username;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getRol() {
        return rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // INSTANCIAR LA CONEXIÓN A LA BASE DE DATOS
    ConexionBDD conectar = new ConexionBDD();
    Connection conectado = (Connection) conectar.conectar();
    PreparedStatement ejecutar;
    ResultSet resultado;

    // MÉTODOS DE TRANSACCIONABILIDAD (CRUD)
    public int insertarUsuario() {
        int idGenerado = -1;
        // 6 parámetros de entrada (?) y 1 parámetro de salida (?) para el ID
        String sentenciaSQL = "{call sp_crear_usuario(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
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
                System.out.println("Usuario creado en la BDD");
            } else {
                System.out.println("El usuario no se pudo crear.");
            }

        } catch (SQLException e) {
            System.out.println("Error en el conector MySQL JDBC: " + e.getMessage());
        }

        return idGenerado;
    }

    public ArrayList<String[]> obtenerUsuarios() {
        ArrayList<String[]> lregistros = new ArrayList<>();

        try {
            String sentenciaSQL = "{call sp_listar_usuarios()}";
            ejecutar = conectado.prepareCall(sentenciaSQL);
            ResultSet res = ejecutar.executeQuery();

            while (res.next()) {
                String[] listaUsuarios = new String[7];
                listaUsuarios[0] = res.getInt("id") + "";
                listaUsuarios[1] = res.getString("nombre");
                listaUsuarios[2] = res.getString("email");
                listaUsuarios[3] = res.getString("telefono");
                listaUsuarios[4] = res.getString("usuario");
                listaUsuarios[5] = res.getString("contrasena");
                listaUsuarios[6] = res.getString("rol");
                lregistros.add(listaUsuarios);
            }
            ejecutar.close();
            return lregistros;
        } catch (SQLException e) {
            System.out.println("------" + e);
        }
        return lregistros;
    }

    public boolean actualizarUsuario() {
        int filasA = 0;
        String sentenciaSQL = "{call sp_actualizar_usuario(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.setString(2, nombre);
            ejecutar.setString(3, email);
            ejecutar.setString(4, telefono);
            ejecutar.setString(5, usuario);
            ejecutar.setString(6, contrasena);
            ejecutar.registerOutParameter(7, Types.INTEGER);

            ejecutar.execute();
            filasA = ejecutar.getInt(7);

            if (filasA > 0) {
                System.out.println("Usuario actualizado en la BDD");
            } else {
                System.out.println("No se encontró el usuario a actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
        return filasA > 0;
    }

    public boolean inhabilitarUsuario() {
        int filasI = 0;
        String sentenciaSQL = "{call sp_inhabilitar_usuario(?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.registerOutParameter(2, Types.INTEGER);

            ejecutar.execute();
            filasI = ejecutar.getInt(2);

            if (filasI > 0) {
                System.out.println("Usuario inhabilitado en la BDD");
            } else {
                System.out.println("No se encontró el usuario a inhabilitar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inhabilitar usuario: " + e.getMessage());
        }
        return filasI > 0;
    }

}

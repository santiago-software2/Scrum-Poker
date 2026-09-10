/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Desarrollador;
import Modelo.ProductOwner;
import Modelo.Requerimiento;
import Modelo.Usuario;
import Vista.RequerimientoVista;
import Vista.UsuarioVista;
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
public class UsuarioControlador {

    // REFERENCIA AL MODELO Y A LA VISTA
    private Usuario umodelo;
    private UsuarioVista uvista;
//    int cont = 1;
    // CONSTRUCTORS
    public UsuarioControlador() {
    }

    public UsuarioControlador(Usuario umodelo, UsuarioVista uvista) {
        this.umodelo = umodelo;
        this.uvista = uvista;
    }
    
    // Registrar un nuevo usuario (Desarrollador o ProductOwner)
    public void registrarUsuario() {
        String nombre = uvista.getTxtNombre();
        String email = uvista.getTxtEmail();
        String contrasena = uvista.getTxtContrasena();
        String rol = uvista.getCmbRol().getSelectedItem().toString(); 

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || rol.isEmpty()) {
            System.out.println("Por favor complete todos los campos.");
            return;
        }

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_crear_usuario(?,?,?,?,?)}");
            cs.setString(1, nombre);
            cs.setString(2, email);
            cs.setString(3, contrasena);
            cs.setString(4, rol);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.execute();

            if (cs.getInt(5) > 0) {
                uvista.limpiarCampos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario iniciarSesion(String email, String contrasena) {
        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_validar_usuario(?,?)}");
            cs.setString(1, email);
            cs.setString(2, contrasena);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                Usuario usuario = rs.getString("rol").equals("ProductOwner")
                        ? new ProductOwner()
                        : new Desarrollador();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(email);
                usuario.setRol(rs.getString("rol"));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // credenciales incorrectas
    }

    public void iniciar() {
        uvista.getBtnCrear().addActionListener(e -> registrarUsuario());
        uvista.setVisible(true);
    }

}

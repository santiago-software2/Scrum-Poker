/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Vista;

import Controlador.ConexionBDD;
import Controlador.UsuarioControlador;
import Modelo.Desarrollador;
import Modelo.Usuario;
import Vista.UsuarioVista;
import java.sql.Connection;

/**
 *
 * @author SUPERTRONICA
 */
public class Main {

    public static void main(String[] args) {

        Connection conexion = ConexionBDD.getConexion();

        if (conexion != null) {
            System.out.println("Prueba desde Main: conexion obtenida correctamente.");
        } else {
            System.out.println("Prueba desde Main: no se pudo conectar.");
        }
    }
}
//        Usuario modelo = new Desarrollador();
//        UsuarioVista vista = new UsuarioVista();
//        UsuarioControlador controlador = new UsuarioControlador(modelo, vista);
//
//        controlador.iniciar(); // Esto enlaza los botones y muestra la ventana
//    }


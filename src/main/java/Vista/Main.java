/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Vista;

import Controlador.UsuarioControlador;
import Modelo.Desarrollador;
import Modelo.Usuario;
import Vista.UsuarioVista;

/**
 *
 * @author SUPERTRONICA
 */
public class Main {

    public static void main(String[] args) {
        Usuario modelo = new Desarrollador();
        UsuarioVista vista = new UsuarioVista();
        UsuarioControlador controlador = new UsuarioControlador(modelo, vista);

        controlador.iniciar(); // Esto enlaza los botones y muestra la ventana
    }
}

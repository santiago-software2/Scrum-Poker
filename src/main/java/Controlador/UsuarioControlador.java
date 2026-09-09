/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Desarrollador;
import Modelo.ProductOwner;
import Modelo.Usuario;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class UsuarioControlador {
    // REFERENCIA A LA VISTA
    private UsuarioVista uvista;

    // CONSTRUCTORES
    public UsuarioControlador() {
    }

    public UsuarioControlador(Usuario umodelo, UsuarioVista uvista) {
        this.uvista = uvista;
    }

    // CARGAR LA TABLA EN LA VISTA
    public void cargarDatosTabla() {
        uvista.getModelo().setRowCount(0);

        // Instanciamos temporalmente para traer los datos generales de la BD
        Desarrollador temp = new Desarrollador();
        ArrayList<String[]> lUsuarios = temp.obtenerUsuarios();
        
        if (lUsuarios != null) {
            for (String[] ur : lUsuarios) {
                Object[] fila = {ur[0], ur[1], ur[2], ur[3], ur[4], ur[5], ur[6], ur[7]};
                uvista.getModelo().addRow(fila);
            }
        }
    }

    // RECUPERAR LOS DATOS DE LA VISTA E INSERTAR
    public void agregarUsuario() {
        String nombres = uvista.getTxtNombres().getText();
        String email = uvista.getTxtEmail().getText();
        String telefono = uvista.getTxtTelefono().getText();
        String usuarioStr = uvista.getTxtUsuario().getText();
        String contrasena = uvista.getTxtContrasena().getText();
        String rol = uvista.getCmbRol().getSelectedItem().toString();

        if (!nombres.isEmpty() && !email.isEmpty() && !usuarioStr.isEmpty() && !contrasena.isEmpty()) {
            Usuario nuevoUsuario;

            // Dependiendo del rol seleccionado, creamos una instancia hija
            if (rol.equalsIgnoreCase("Desarrollador")) {
                nuevoUsuario = new Desarrollador();
            } else {
                nuevoUsuario = new ProductOwner();
            }

            nuevoUsuario.setNombre(nombres);
            nuevoUsuario.setEmail(email);
            // Setea el resto de atributos según los setters que tengas en Usuario o sus clases hijas

            int idGenerado = nuevoUsuario.insertarUsuario();

            if (idGenerado > -1) {
                cargarDatosTabla();
                uvista.limpiarCampos();
            }

        } else {
            System.out.println("Por favor complete los campos obligatorios.");
        }
    }

    public void seleccionarFila() {
        int filaSeleccionada = uvista.getTblUsuarios().getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombres = uvista.getModelo().getValueAt(filaSeleccionada, 1).toString();
            String email = uvista.getModelo().getValueAt(filaSeleccionada, 2).toString();
            String telefono = uvista.getModelo().getValueAt(filaSeleccionada, 3).toString();
            String usuarioStr = uvista.getModelo().getValueAt(filaSeleccionada, 4).toString();
            
            uvista.getTxtNombres().setText(nombres);
            uvista.getTxtEmail().setText(email);
            uvista.getTxtTelefono().setText(telefono);
            uvista.getTxtUsuario().setText(usuarioStr);
        }
    }

    // ACTUALIZAR USUARIO SELECCIONADO
    public void actualizarUsuario() {
        int filaSeleccionada = uvista.getTblUsuarios().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un usuario de la tabla para actualizar.");
            return;
        }

        int id = Integer.parseInt(uvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        String nombres = uvista.getTxtNombres().getText();

        if (!nombres.isEmpty()) {
            Desarrollador u = new Desarrollador();
            u.setId(id);
            u.setNombre(nombres);

            boolean actualizado = u.actualizarUsuario();

            if (actualizado) {
                cargarDatosTabla();
                uvista.limpiarCampos();
                uvista.getTblUsuarios().clearSelection();
            }
        } else {
            System.out.println("Por favor complete los campos requeridos.");
        }
    }

    // INHABILITAR USUARIO SELECCIONADO
    public void inhabilitarUsuario() {
        int filaSeleccionada = uvista.getTblUsuarios().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un usuario de la tabla para inhabilitar.");
            return;
        }

        int id = Integer.parseInt(uvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        Desarrollador u = new Desarrollador();
        u.setId(id);

        boolean inhabilitado = u.inhabilitarUsuario();

        if (inhabilitado) {
            cargarDatosTabla();
            uvista.getTblUsuarios().clearSelection();
        }
    }

    public void iniciar() {
        uvista.getBtnCrear().addActionListener(e -> agregarUsuario());
        uvista.getBtnMostrar().addActionListener(e -> cargarDatosTabla());
        uvista.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        uvista.getBtnInhabilitar().addActionListener(e -> inhabilitarUsuario());
        uvista.getTblUsuarios().getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        uvista.setVisible(true);
        this.cargarDatosTabla();
    }
}

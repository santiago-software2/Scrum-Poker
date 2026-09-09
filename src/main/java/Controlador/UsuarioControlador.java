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
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class UsuarioControlador {

    // REFERENCIA AL MODELO Y A LA VISTA
    private Usuario umodelo;
    private UsuarioVista uvista;

    // CONSTRUCTORS
    public UsuarioControlador() {
    }

    public UsuarioControlador(Usuario umodelo, UsuarioVista uvista) {
        this.umodelo = umodelo; // <-- ¡Aquí faltaba guardar la referencia del modelo!
        this.uvista = uvista;
    }

    // CARGAR LA TABLA EN LA VISTA
    public void cargarDatosTabla() {
        uvista.getModelo().setRowCount(0);

        // Verificamos que umodelo no sea nulo; si lo es, instanciamos un Usuario genérico para consultar
        if (umodelo == null) {
            umodelo = new Usuario();
        }

        ArrayList<String[]> lUsuarios = umodelo.obtenerUsuarios();

        if (lUsuarios != null) {
            for (String[] ur : lUsuarios) {
                Object[] fila = {ur[0], ur[1], ur[2], ur[3], ur[4], ur[5], ur[6]};
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
            nuevoUsuario.setTelefono(telefono);
            nuevoUsuario.setUsuario(usuarioStr);
            nuevoUsuario.setContrasena(contrasena);
            nuevoUsuario.setRol(rol);

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
            String contrasena = uvista.getModelo().getValueAt(filaSeleccionada, 5).toString();
            String rol = uvista.getModelo().getValueAt(filaSeleccionada, 6).toString();

            uvista.getTxtNombres().setText(nombres);
            uvista.getTxtEmail().setText(email);
            uvista.getTxtTelefono().setText(telefono);
            uvista.getTxtUsuario().setText(usuarioStr);
            uvista.getTxtContrasena().setText(contrasena);
            uvista.getCmbRol().setSelectedItem(rol);
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
        String email = uvista.getTxtEmail().getText();
        String telefono = uvista.getTxtTelefono().getText();
        String usuarioStr = uvista.getTxtUsuario().getText();
        String contrasena = uvista.getTxtContrasena().getText();

        if (!nombres.isEmpty() && !email.isEmpty() && !usuarioStr.isEmpty()) {
            if (umodelo == null) {
                umodelo = new Desarrollador();
            }
            umodelo.setId(id);
            umodelo.setNombre(nombres);
            umodelo.setEmail(email);
            umodelo.setTelefono(telefono);
            umodelo.setUsuario(usuarioStr);
            umodelo.setContrasena(contrasena);

            boolean actualizado = umodelo.actualizarUsuario();

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
        if (umodelo == null) {
            umodelo = new Desarrollador();
        }
        umodelo.setId(id);

        boolean inhabilitado = umodelo.inhabilitarUsuario();

        if (inhabilitado) {
            cargarDatosTabla();
            uvista.getTblUsuarios().clearSelection();
        }
    }

    public void iniciarSesion() {
        String correo = uvista.getTxtUsuario().getText();
        String password = new String(uvista.getJPasswordField1().getPassword());

        if (!correo.isEmpty() && !password.isEmpty()) {
            boolean acceso = umodelo.validarUsuario(correo, password);

            if (acceso) {
                System.out.println("Acceso concedido");
                uvista.dispose();

                // Aquí abres tu siguiente ventana principal (ej. Requerimientos)
                RequerimientoVista rv = new RequerimientoVista();
                Requerimiento rm = new Requerimiento();
                RequerimientoControlador rc = new RequerimientoControlador(rm, rv);
                rc.iniciar();

            } else {
                System.out.println("Correo o contraseña incorrectos.");
            }
        } else {
            System.out.println("Por favor complete todos los campos.");
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

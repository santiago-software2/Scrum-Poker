/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Sala;
import Vista.SalaVista;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class SalaControlador {
    // REFERENCIA AL MODELO Y A LA VISTA
    private Sala smodelo;
    private SalaVista svista;
    int cont = 1;

    // CONSTRUCTORES
    public SalaControlador() {
    }

    public SalaControlador(Sala smodelo, SalaVista svista) {
        this.smodelo = smodelo;
        this.svista = svista;
    }

    // CARGAR LA TABLA EN LA VISTA
    public void cargarDatosTabla() {
        svista.getModelo().setRowCount(0);

        ArrayList<String[]> lSalas = smodelo.obtenerSalas();
        for (String[] sr : lSalas) {
            Object[] fila = {sr[0], sr[1], sr[2]};
            svista.getModelo().addRow(fila);
            cont++;
        }
    }

    // RECUPERAR LOS DATOS DE LA VISTA E INSERTAR
    public void agregarSala() {
        String nombre = svista.getTxtNombre();
        String descripcion = svista.getTxtDescripcion();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            smodelo.setNombre(nombre);
            smodelo.setDescripcion(descripcion);

            int idGenerado = smodelo.insertarSalas();

            if (idGenerado > -1) {
                cargarDatosTabla();
                svista.limpiarCampos();
            }

        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    public void seleccionarFila() {
        int filaSeleccionada = svista.getTblSalas().getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombre = svista.getModelo().getValueAt(filaSeleccionada, 1).toString();
            String descripcion = svista.getModelo().getValueAt(filaSeleccionada, 2).toString();

            svista.setTxtNombre(nombre);
            svista.setTxtDescripcion(descripcion);
        }
    }

    // ACTUALIZAR SALA SELECCIONADA
    public void actualizarSala() {
        int filaSeleccionada = svista.getTblSalas().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione una sala de la tabla para actualizar.");
            return;
        }

        int id = Integer.parseInt(svista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        String nombre = svista.getTxtNombre();
        String descripcion = svista.getTxtDescripcion();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            smodelo.setId(id);
            smodelo.setNombre(nombre);
            smodelo.setDescripcion(descripcion);

            boolean actualizado = smodelo.actualizarSala();

            if (actualizado) {
                cargarDatosTabla();
                svista.limpiarCampos();
                svista.getTblSalas().clearSelection();
            }
        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    // INHABILITAR SALA SELECCIONADA
    public void inhabilitarSala() {
        int filaSeleccionada = svista.getTblSalas().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione una sala de la tabla para inhabilitar.");
            return;
        }

        int id = Integer.parseInt(svista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        smodelo.setId(id);

        boolean inhabilitado = smodelo.inhabilitarSala();

        if (inhabilitado) {
            cargarDatosTabla();
            svista.getTblSalas().clearSelection();
        }
    }

    public void iniciar() {
        svista.getBtnCrear().addActionListener(e -> agregarSala());
        svista.getBtnMostrar().addActionListener(e -> cargarDatosTabla());
        svista.getBtnActualizar().addActionListener(e -> actualizarSala());
        svista.getBtnInhabilitar().addActionListener(e -> inhabilitarSala());
        svista.getTblSalas().getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        svista.setVisible(true);
        this.cargarDatosTabla();
    }
}

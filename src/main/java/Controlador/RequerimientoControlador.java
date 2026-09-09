/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Requerimiento;
import Vista.RequerimientoVista;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class RequerimientoControlador {
    
    // REFERENCIA A MODELO Y A LA VISTA
    private Requerimiento rmodelo;
    private RequerimientoVista rvista;
    int cont = 1;
    
    // CONSTRUCTORES
    public RequerimientoControlador() {
    }

    public RequerimientoControlador(Requerimiento rmodelo, RequerimientoVista rvista) {
        this.rmodelo = rmodelo;
        this.rvista = rvista;
    }
    
    // CARGAR LA TABLA EN LA VISTA
    public void cargarDatosTabla() {
        rvista.getModelo().setRowCount(0);

        ArrayList<String[]> lRequerimientos = rmodelo.obtenerRequerimientos();
        for (String[] rq : lRequerimientos) {
            Object[] fila = {rq[0], rq[1], rq[2], rq[3]};
            rvista.getModelo().addRow(fila);
            cont++;
        }
    }

    // RECUPERAR LOS DATOS DE LA VISTA E INSERTAR
    public void agregarRequerimiento() {
        String titulo = rvista.getTxtTitulo();
        String estado = rvista.getTxtEstado();

        if (!titulo.isEmpty() && !estado.isEmpty()) {
            rmodelo.setTitulo(titulo);
            rmodelo.setEstado(estado);

            int idGenerado = rmodelo.insertarRequerimientos();

            if (idGenerado > -1) {
                cargarDatosTabla();
                rvista.limpiarCampos();
            }

        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    public void seleccionarFila() {
        int filaSeleccionada = rvista.getTblRequerimientos().getSelectedRow();
        if (filaSeleccionada != -1) {
            String titulo = rvista.getModelo().getValueAt(filaSeleccionada, 1).toString();
            String estado = rvista.getModelo().getValueAt(filaSeleccionada, 3).toString();

            rvista.setTxtTitulo(titulo);
            rvista.setTxtEstado(estado);
        }
    }

    // ACTUALIZAR REQUERIMIENTO SELECCIONADO
    public void actualizarRequerimiento() {
        int filaSeleccionada = rvista.getTblRequerimientos().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un requerimiento de la tabla para actualizar.");
            return;
        }

        int id = Integer.parseInt(rvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        String titulo = rvista.getTxtTitulo();
        String estado = rvista.getTxtEstado();

        if (!titulo.isEmpty() && !estado.isEmpty()) {
            rmodelo.setId(id);
            rmodelo.setTitulo(titulo);
            rmodelo.setEstado(estado);

            boolean actualizado = rmodelo.actualizarRequerimiento();

            if (actualizado) {
                cargarDatosTabla();
                rvista.limpiarCampos();
                rvista.getTblRequerimientos().clearSelection();
            }
        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    // INHABILITAR REQUERIMIENTO SELECCIONADO
    public void inhabilitarRequerimiento() {
        int filaSeleccionada = rvista.getTblRequerimientos().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un requerimiento de la tabla para inhabilitar.");
            return;
        }

        int id = Integer.parseInt(rvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        rmodelo.setId(id);

        boolean inhabilitado = rmodelo.inhabilitarRequerimiento();

        if (inhabilitado) {
            cargarDatosTabla();
            rvista.getTblRequerimientos().clearSelection();
        }
    }

    public void iniciar() {
        rvista.getBtnCrear().addActionListener(e -> agregarRequerimiento());
        rvista.getBtnMostrar().addActionListener(e -> cargarDatosTabla());
        rvista.getBtnActualizar().addActionListener(e -> actualizarRequerimiento());
        rvista.getBtnInhabilitar().addActionListener(e -> inhabilitarRequerimiento());
        rvista.getTblRequerimientos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });
        rvista.setVisible(true);
        this.cargarDatosTabla();
    }
}

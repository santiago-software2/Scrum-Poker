/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Voto;
import Vista.VotoVista;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class VotoControlador {

    private Voto vmodelo;
    private VotoVista vvista;
    int cont = 1;

    private int idUsuarioActual;
    private int idRequerimientoActual;
    

// Constructor para recibir los datos de sesión y del requerimiento
    public VotoControlador(VotoVista vvista, Voto vmodelo, int idUsuario, int idRequerimiento) {
        this.vvista = vvista;
        this.vmodelo = vmodelo;
        this.idUsuarioActual = idUsuario;
        this.idRequerimientoActual = idRequerimiento;
        iniciar();
    }

    public VotoControlador() {
    }

    public VotoControlador(Voto vmodelo, VotoVista vvista) {
        this.vmodelo = vmodelo;
        this.vvista = vvista;
    }

    public void cargarDatosTabla() {
        vvista.getModelo().setRowCount(0);

        ArrayList<String[]> lVotos = vmodelo.obtenerVotos();
        for (String[] vr : lVotos) {
            Object[] fila = {vr[0], vr[1]};
            vvista.getModelo().addRow(fila);
            cont++;
        }
    }

    public void agregarVoto() {
        String valorVoto = vvista.getTxtValor();

        if (!valorVoto.isEmpty()) {
            vmodelo.setValor(valorVoto);

            int idGenerado = vmodelo.insertarVotos();

            if (idGenerado > -1) {
                cargarDatosTabla();
                vvista.limpiarCampos();
            }

        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    public void seleccionarFila() {
        int filaSeleccionada = vvista.getTblVotos().getSelectedRow();
        if (filaSeleccionada != -1) {
            String valor = vvista.getModelo().getValueAt(filaSeleccionada, 1).toString();
            vvista.setTxtValor(valor);
        }
    }

    public void actualizarVoto() {
        int filaSeleccionada = vvista.getTblVotos().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un voto de la tabla para actualizar.");
            return;
        }

        int id = Integer.parseInt(vvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        String valor = vvista.getTxtValor();

        if (!valor.isEmpty()) {
            vmodelo.setId(id);
            vmodelo.setValor(valor);

            boolean actualizado = vmodelo.actualizarVoto();

            if (actualizado) {
                cargarDatosTabla();
                vvista.limpiarCampos();
                vvista.getTblVotos().clearSelection();
            }
        } else {
            System.out.println("Por favor complete todos los campos.");
        }
    }

    public void inhabilitarVoto() {
        int filaSeleccionada = vvista.getTblVotos().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un voto de la tabla para inhabilitar.");
            return;
        }

        int id = Integer.parseInt(vvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        vmodelo.setId(id);

        boolean inhabilitado = vmodelo.inhabilitarVoto();

        if (inhabilitado) {
            cargarDatosTabla();
            vvista.getTblVotos().clearSelection();
        }
    }

    public void emitirVoto() {
        String puntosStr = vvista.getCmbPuntos().getSelectedItem().toString();
        int valorVoto = Integer.parseInt(puntosStr);

        Voto nuevoVoto = new Voto();
        nuevoVoto.setIdUsuario(this.idUsuarioActual);
        nuevoVoto.setIdRequerimiento(this.idRequerimientoActual);
        nuevoVoto.setValor(valorVoto);

        boolean registrado = nuevoVoto.registrarVoto();

        if (registrado) {
            System.out.println("Voto registrado con éxito en la sesión de Scrum Poker.");
            vvista.mostrarMensaje("¡Voto emitido!");
        } else {
            System.out.println("Error al registrar el voto.");
        }
    }

    public void iniciar() {
        vvista.getBtnMostrar().addActionListener(e -> cargarDatosTabla());
        vvista.getTblVotos().getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        vvista.setVisible(true);
        this.cargarDatosTabla();
    }
}

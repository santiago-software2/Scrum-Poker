/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.ProductOwner;
import Modelo.Requerimiento;
import Modelo.Sala;
import Vista.RequerimientoVista;
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
public class RequerimientoControlador {

    // REFERENCIA A MODELO Y A LA VISTA
    private Requerimiento rmodelo;
    private RequerimientoVista rvista;
    private Sala salaActual;
    private ProductOwner productOwnerActual;
//    int cont = 1;

    // CONSTRUCTORES
    public RequerimientoControlador() {
    }

    public RequerimientoControlador(Requerimiento rmodelo, RequerimientoVista rvista,
            Sala salaActual, ProductOwner productOwnerActual) {
        this.rmodelo = rmodelo;
        this.rvista = rvista;
        this.salaActual = salaActual;
        this.productOwnerActual = productOwnerActual;
    }

    // CREAR requerimiento
    public void crearRequerimiento() {
        String titulo = rvista.getTxtTitulo();

        if (titulo.isEmpty()) {
            System.out.println("Por favor ingrese el titulo del requerimiento.");
            return;
        }

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_crear_requerimiento(?,?,?,?)}");
            cs.setInt(1, salaActual.getId());
            cs.setInt(2, productOwnerActual.getId());
            cs.setString(3, titulo);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();

            if (cs.getInt(4) > 0) {
                cargarDatosTabla();
                rvista.limpiarCampos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LISTAR requerimientos de la sala actual
    public void cargarDatosTabla() {
        rvista.getModelo().setRowCount(0);

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_listar_requerimientos(?)}");
            cs.setInt(1, salaActual.getId());
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("estado"),
                    rs.getString("estimacion_final")
                };
                rvista.getModelo().addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ACTUALIZAR requerimiento seleccionado
    public void actualizarRequerimiento() {
        int filaSeleccionada = rvista.getTblRequerimientos().getSelectedRow();
        if (filaSeleccionada == -1) {
            rvista.mostrarMensaje("Seleccione un requerimiento de la tabla para actualizar.");
            return;
        }

        String titulo = rvista.getTxtTitulo();
        if (titulo.isEmpty()) {
            rvista.mostrarMensaje("El titulo no puede estar vacio.");
            return;
        }

        int id = Integer.parseInt(rvista.getModelo().getValueAt(filaSeleccionada, 0).toString());
        String estado = rvista.getCmbEstado().getSelectedItem().toString();
        String estimacionFinal = rvista.getTxtEstimacionFinal();

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_actualizar_requerimiento(?,?,?,?,?)}");
            cs.setInt(1, id);
            cs.setString(2, titulo);
            cs.setString(3, estado);
            cs.setString(4, estimacionFinal.isEmpty() ? null : estimacionFinal);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.execute();

            if (cs.getInt(5) > 0) {
                cargarDatosTabla();
                rvista.limpiarCampos();
                rvista.getTblRequerimientos().clearSelection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INHABILITAR requerimiento seleccionado
    public void inhabilitarRequerimiento() {
        int filaSeleccionada = rvista.getTblRequerimientos().getSelectedRow();
        if (filaSeleccionada == -1) {
            System.out.println("Seleccione un requerimiento de la tabla para inhabilitar.");
            return;
        }

        int id = Integer.parseInt(rvista.getModelo().getValueAt(filaSeleccionada, 0).toString());

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_inhabilitar_requerimiento(?,?)}");
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            if (cs.getInt(2) > 0) {
                cargarDatosTabla();
                rvista.getTblRequerimientos().clearSelection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarFila() {
        int fila = rvista.getTblRequerimientos().getSelectedRow();
        if (fila != -1) {
            rvista.setTxtTitulo(rvista.getModelo().getValueAt(fila, 1).toString());
            rvista.setCmbEstado(rvista.getModelo().getValueAt(fila, 2).toString());
            Object estimacion = rvista.getModelo().getValueAt(fila, 3);
            rvista.setTxtEstimacionFinal(estimacion == null ? "" : estimacion.toString());
        }
    }

    public void iniciar() {
        rvista.getBtnCrear().addActionListener(e -> crearRequerimiento());
        rvista.getBtnActualizar().addActionListener(e -> actualizarRequerimiento());
        rvista.getBtnInhabilitar().addActionListener(e -> inhabilitarRequerimiento());
        rvista.getTblRequerimientos().getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        rvista.getTblRequerimientos().getSelectionModel().addListSelectionListener(e -> {
        });
        rvista.setVisible(true);
        cargarDatosTabla();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Desarrollador;
import Modelo.Requerimiento;
import Modelo.Voto;
import Vista.VotoVista;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class VotoControlador {

    private Voto vmodelo;
    private VotoVista vvista;
    private Desarrollador desarrolladorActual;
    private Requerimiento requerimientoActual;
//    int cont = 1;

    public VotoControlador() {
    }

    public VotoControlador(Voto vmodelo, VotoVista vvista,
            Desarrollador desarrolladorActual, Requerimiento requerimientoActual) {
        this.vmodelo = vmodelo;
        this.vvista = vvista;
        this.desarrolladorActual = desarrolladorActual;
        this.requerimientoActual = requerimientoActual;
    }

    // REGISTRAR (o actualizar) el voto del desarrollador actual
    public void registrarVoto() {
        String carta = vvista.getCmbCarta().getSelectedItem().toString();

        if (carta.isEmpty()) {
            System.out.println("Seleccione una carta para votar.");
            return;
        }

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_registrar_voto(?,?,?)}");
            cs.setInt(1, requerimientoActual.getId());
            cs.setInt(2, desarrolladorActual.getId());
            cs.setString(3, carta);
            cs.execute();

            vvista.mostrarMensaje("Voto registrado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // REVELAR los votos del requerimiento actual
    public void revelarVotos() {
        vvista.getModelo().setRowCount(0);

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_revelar_votos(?)}");
            cs.setInt(1, requerimientoActual.getId());
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("nombre"),
                    rs.getString("carta"),
                    rs.getTimestamp("fecha")
                };
                vvista.getModelo().addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void iniciar() {
        vvista.getBtnVotar().addActionListener(e -> registrarVoto());
        vvista.getBtnRevelar().addActionListener(e -> revelarVotos());
        vvista.setVisible(true);
    }
}

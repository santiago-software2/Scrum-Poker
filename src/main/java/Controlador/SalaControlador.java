/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Sala;
import Vista.SalaVista;
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
public class SalaControlador {

    // REFERENCIA AL MODELO Y A LA VISTA
    private Sala smodelo;
    private SalaVista svista;
//    int cont = 1;

    // CONSTRUCTORES
    public SalaControlador() {
    }

    public SalaControlador(Sala smodelo, SalaVista svista) {
        this.smodelo = smodelo;
        this.svista = svista;
    }

    // CREAR sala (la crea el ProductOwner)
    public void crearSala() {
        String codigoAcceso = svista.getTxtCodigoAcceso();

        if (codigoAcceso.isEmpty()) {
            System.out.println("Ingrese un codigo de acceso para la sala.");
            return;
        }

        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_crear_sala(?,?)}");
            cs.setString(1, codigoAcceso);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            int idGenerado = cs.getInt(2);
            if (idGenerado > 0) {
                smodelo.setId(idGenerado);
                smodelo.setCodigoAcceso(codigoAcceso);
                smodelo.setEstado("Activa");
                svista.mostrarMensaje("Sala creada. Codigo: " + codigoAcceso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UNIRSE a una sala existente (la usa el Desarrollador) buscando por codigo
    public Sala unirseASala(String codigoAcceso) {
        try (Connection con = ConexionBDD.getConexion()) {
            CallableStatement cs = con.prepareCall("{call sp_buscar_sala(?)}");
            cs.setString(1, codigoAcceso);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setCodigoAcceso(rs.getString("codigo_acceso"));
                sala.setEstado(rs.getString("estado"));
                return sala;
            } else {
                svista.mostrarMensaje("No existe una sala con ese codigo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void iniciar() {
        svista.getBtnCrear().addActionListener(e -> crearSala());
        svista.getBtnUnirse().addActionListener(e -> unirseASala(svista.getTxtCodigoAcceso()));
        svista.setVisible(true);
    }
}

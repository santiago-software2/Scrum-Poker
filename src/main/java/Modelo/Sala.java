/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.ConexionBDD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class Sala {

    private int id;
    private String codigoAcceso;
    private String estado;

    public Sala() {
    }

    public Sala(int id, String codigoAcceso, String estado) {
        this.id = id;
        this.codigoAcceso = codigoAcceso;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
//    // INSTANCIAR LA CONEXIÓN A LA BASE DE DATOS
//    ConexionBDD conectar = new ConexionBDD();
//    Connection conectado = (Connection) conectar.conectar();
//    PreparedStatement ejecutar;
//    ResultSet resultado;
//
//    // MÉTODOS DE TRANSACCIONABILIDAD
//    public int insertarSalas() {
//        int idGenerado = -1;
//        String sentenciaSQL = "{call sp_crear_sala(?, ?, ?)}";
//        
//        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
//            ejecutar.setString(1, nombre);
//            ejecutar.setString(2, descripcion);
//            ejecutar.registerOutParameter(3, Types.INTEGER);
//
//            ejecutar.execute();
//            idGenerado = ejecutar.getInt(3);
//
//            if (idGenerado > -1) {
//                System.out.println("Sala creada en la BDD");
//            } else {
//                System.out.println("La sala no se pudo crear.");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error en el conector MySQL JDBC: " + e.getMessage());
//        }
//
//        return idGenerado;
//    }
//
//    public ArrayList<String[]> obtenerSalas() {
//        ArrayList<String[]> lregistros = new ArrayList<>();
//
//        try {
//            String sentenciaSQL = "{call sp_mostrar_salas()}";
//            ejecutar = conectado.prepareCall(sentenciaSQL);
//            ResultSet res = ejecutar.executeQuery();
//
//            while (res.next()) {
//                String[] listaSalas = new String[3];
//                listaSalas[0] = res.getInt("id") + "";
//                listaSalas[1] = res.getString("nombre");
//                listaSalas[2] = res.getString("descripcion");
//                lregistros.add(listaSalas);
//            }
//            ejecutar.close();
//            return lregistros;
//        } catch (SQLException e) {
//            System.out.println("------" + e);
//        }
//        return lregistros;
//    }
//
//    public boolean actualizarSala() {
//        int filasA = 0;
//        String sentenciaSQL = "{call sp_actualizar_sala(?, ?, ?, ?)}";
//        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
//
//            ejecutar.setInt(1, id);
//            ejecutar.setString(2, nombre);
//            ejecutar.setString(3, descripcion);
//            ejecutar.registerOutParameter(4, Types.INTEGER);
//
//            ejecutar.execute();
//            filasA = ejecutar.getInt(4);
//
//            if (filasA > 0) {
//                System.out.println("Sala actualizada en la BDD");
//            } else {
//                System.out.println("No se encontró la sala a actualizar.");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al actualizar sala: " + e.getMessage());
//        }
//        return filasA > 0;
//    }
//
//    public boolean inhabilitarSala() {
//        int filasI = 0;
//        String sentenciaSQL = "{call sp_inhabilitar_sala(?, ?)}";
//        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
//
//            ejecutar.setInt(1, id);
//            ejecutar.registerOutParameter(2, Types.INTEGER);
//
//            ejecutar.execute();
//            filasI = ejecutar.getInt(2);
//
//            if (filasI > 0) {
//                System.out.println("Sala inhabilitada en la BDD");
//            } else {
//                System.out.println("No se encontró la sala a inhabilitar.");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al inhabilitar sala: " + e.getMessage());
//        }
//        return filasI > 0;
//    }

}

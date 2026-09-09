/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Controlador.ConexionBDD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author SUPERTRONICA
 */
public class Voto {

    private int id;
    private String valor;
    private Date fecha;
    private Desarrollador desarrollador;
    private Requerimiento requerimiento;

    public Voto() {
    }

    public Voto(int id, String valor, Date fecha, Desarrollador desarrollador, Requerimiento requerimiento, PreparedStatement ejecutar, ResultSet resultado) {
        this.id = id;
        this.valor = valor;
        this.fecha = fecha;
        this.desarrollador = desarrollador;
        this.requerimiento = requerimiento;
        this.ejecutar = ejecutar;
        this.resultado = resultado;
    }

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public Requerimiento getRequerimiento() {
        return requerimiento;
    }

    public String getValor() {
        return valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public void setRequerimiento(Requerimiento requerimiento) {
        this.requerimiento = requerimiento;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    // INSTANCIAR LA CONEXIÓN A LA BASE DE DATOS
    ConexionBDD conectar = new ConexionBDD();
    Connection conectado = (Connection) conectar.conectar();
    PreparedStatement ejecutar;
    ResultSet resultado;

    // MÉTODOS DE TRANSACCIONABILIDAD
    public int insertarVotos() {
        int idGenerado = -1;
        String sentenciaSQL = "{call sp_crear_voto(?, ?)}";

        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
            ejecutar.setString(1, valor);
            ejecutar.registerOutParameter(2, Types.INTEGER);

            ejecutar.execute();
            idGenerado = ejecutar.getInt(2);

            if (idGenerado > -1) {
                System.out.println("Voto creado en la BDD");
            } else {
                System.out.println("El voto no se pudo crear.");
            }

        } catch (SQLException e) {
            System.out.println("Error en el conector MySQL JDBC: " + e.getMessage());
        }

        return idGenerado;
    }

    public ArrayList<String[]> obtenerVotos() {
        ArrayList<String[]> lregistros = new ArrayList<>();

        try {
            String sentenciaSQL = "{call sp_mostrar_votos()}";
            ejecutar = conectado.prepareCall(sentenciaSQL);
            ResultSet res = ejecutar.executeQuery();

            while (res.next()) {
                String[] listaVotos = new String[4];
                listaVotos[0] = res.getInt("id") + "";
                listaVotos[1] = res.getString("valor");
                // Ajusta las columnas adicionales según tu base de datos si requieres fecha/desarrollador
                listaVotos[2] = "";
                listaVotos[3] = "";
                lregistros.add(listaVotos);
            }
            ejecutar.close();
            return lregistros;
        } catch (SQLException e) {
            System.out.println("------" + e);
        }
        return lregistros;
    }

    public boolean actualizarVoto() {
        int filasA = 0;
        String sentenciaSQL = "{call sp_actualizar_voto(?, ?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.setString(2, valor);
            ejecutar.registerOutParameter(3, Types.INTEGER);

            ejecutar.execute();
            filasA = ejecutar.getInt(3);

            if (filasA > 0) {
                System.out.println("Voto actualizado en la BDD");
            } else {
                System.out.println("No se encontró el voto a actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar voto: " + e.getMessage());
        }
        return filasA > 0;
    }

    public boolean inhabilitarVoto() {
        int filasI = 0;
        String sentenciaSQL = "{call sp_inhabilitar_voto(?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.registerOutParameter(2, Types.INTEGER);

            ejecutar.execute();
            filasI = ejecutar.getInt(2);

            if (filasI > 0) {
                System.out.println("Voto inhabilitado en la BDD");
            } else {
                System.out.println("No se encontró el voto a inhabilitar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inhabilitar voto: " + e.getMessage());
        }
        return filasI > 0;
    }

}

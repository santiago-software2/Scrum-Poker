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
public class Requerimiento {

    private int id;
    private String titulo;
    private String estado;
    private ProductOwner productOwner;
    private Sala sala;

    public Requerimiento() {
    }

    public Requerimiento(int id, String titulo, String estado, ProductOwner productOwner, Sala sala) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
        this.productOwner = productOwner;
        this.sala = sala;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEstado() {
        return estado;
    }

    public ProductOwner getProductOwner() {
        return productOwner;
    }

    public Sala getSala() {
        return sala;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setProductOwner(ProductOwner productOwner) {
        this.productOwner = productOwner;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    //  EX CONTRALADOR 
    //INSTANCIAR LA CONEXIÓN A LA BASE DE DATOS
    ConexionBDD conectar = new ConexionBDD();
    //CLASE QUE ME PERMITA CONECTARME DIRECTAMENTE A MYSQL
    Connection conectado = (Connection) conectar.conectar();
    //CLASE QUE ME PERMITE EJECUTAR MI SENTENCIA SQL
    PreparedStatement ejecutar;
    //OBTENER RESULTADOS DE LA CONSULTA
    ResultSet resultado;

    // MÉTODOS DE TRANSACCIONABILIDAD
    public int insertarRequerimientos() {
        int idGenerado = -1;
        String sentenciaSQL = "{call sp_crear_requerimiento(?, ?, ?)}";

        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {
            ejecutar.setString(1, titulo);
            ejecutar.setString(2, estado);
            ejecutar.registerOutParameter(3, Types.INTEGER);

            ejecutar.execute();
            idGenerado = ejecutar.getInt(3);

            if (idGenerado > -1) {
                System.out.println("Requerimiento creado en la BDD");
            } else {
                System.out.println("El requerimiento no se pudo crear.");
            }

        } catch (SQLException e) {
            System.out.println("Error en el conector MySQL JDBC: " + e.getMessage());
        }

        return idGenerado;
    }

    public ArrayList<String[]> obtenerRequerimientos() {
        ArrayList<String[]> lregistros = new ArrayList<>();

        try {
            String sentenciaSQL = "{call sp_listar_requerimientos()}";
            ejecutar = conectado.prepareCall(sentenciaSQL);
            ResultSet res = ejecutar.executeQuery();

            while (res.next()) {
                String[] listaRequerimientos = new String[5]; 
                listaRequerimientos[0] = res.getInt("id") + "";
                listaRequerimientos[1] = res.getString("titulo");
                listaRequerimientos[2] = res.getString("descripcion"); // Si lo tienes en la BD
                listaRequerimientos[3] = res.getString("criterios");   // Si lo tienes en la BD
                listaRequerimientos[4] = res.getString("estado");
                lregistros.add(listaRequerimientos);
            }
            ejecutar.close();
            return lregistros;
        } catch (SQLException e) {
            System.out.println("------" + e);
        }
        return lregistros;
    }

    public boolean actualizarRequerimiento() {
        int filasA = 0;
        String sentenciaSQL = "{call sp_actualizar_requerimiento(?, ?, ?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.setString(2, titulo);
            ejecutar.setString(3, estado);
            ejecutar.registerOutParameter(4, Types.INTEGER);

            ejecutar.execute();
            filasA = ejecutar.getInt(4);

            if (filasA > 0) {
                System.out.println("Requerimiento actualizado en la BDD");
            } else {
                System.out.println("No se encontró el requerimiento a actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar requerimiento: " + e.getMessage());
        }
        return filasA > 0;
    }

    public boolean inhabilitarRequerimiento() {
        int filasI = 0;
        String sentenciaSQL = "{call sp_inhabilitar_requerimiento(?, ?)}";
        try (CallableStatement ejecutar = conectado.prepareCall(sentenciaSQL)) {

            ejecutar.setInt(1, id);
            ejecutar.registerOutParameter(2, Types.INTEGER);

            ejecutar.execute();
            filasI = ejecutar.getInt(2);

            if (filasI > 0) {
                System.out.println("Requerimiento inhabilitado en la BDD");
            } else {
                System.out.println("No se encontró el requerimiento a inhabilitar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inhabilitar requerimiento: " + e.getMessage());
        }
        return filasI > 0;
    }

}

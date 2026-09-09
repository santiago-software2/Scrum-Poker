/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Date;

/**
 *
 * @author SUPERTRONICA
 */
public class Voto {
    private int id;
    private Date fecha;
    private Desarrollador desarrollador;
    private Requerimiento requerimiento;

    public Voto() {
    }

    public Voto(int id, Date fecha, Desarrollador desarrollador, Requerimiento requerimiento) {
        this.id = id;
        this.fecha = fecha;
        this.desarrollador = desarrollador;
        this.requerimiento = requerimiento;
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
    
    
    
}

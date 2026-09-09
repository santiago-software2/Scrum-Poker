/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

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
    
    
    
}

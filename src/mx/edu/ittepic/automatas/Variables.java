/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.automatas;

/**
 *
 * @author Mannlex21
 */
public class Variables {
    String nombre;
    String valor;
    String tipo;

    // Constructor or setter
    public Variables(String nombre, String valor,String tipo) {
        if (nombre == null || valor == null|| tipo == null) {
            return;
        }
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
    }

    // getters

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }public String getTipo() {
        return tipo;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTipo(String tipoa) {
        this.tipo = tipo;
    }
    public String toString(){
       return valor;
    }
}

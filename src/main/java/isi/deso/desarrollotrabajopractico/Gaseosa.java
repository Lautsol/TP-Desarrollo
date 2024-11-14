package isi.deso.desarrollotrabajopractico;

public class Gaseosa extends Bebida{
    public Gaseosa(){}
    public Gaseosa(int id, String nombre, String descripcion, double precio, Categoria categoria, double volumen){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.volumen = volumen;
    }
    
     public double peso(){
        return volumen * 1.04 + volumen * 0.2;  
    }
    
}

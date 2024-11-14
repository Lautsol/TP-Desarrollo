package isi.deso.desarrollotrabajopractico;

public class Alcohol extends Bebida {
    private double graduacion_alcoholica;
    
    public Alcohol(){
    }
    public Alcohol(int id, String nombre, String descripcion, double precio, Categoria categoria, double volumen, double graduacion_alcoholica){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.volumen = volumen;
        this.graduacion_alcoholica = graduacion_alcoholica;
    }

    public double getGraduacion_alcoholica() {
        return graduacion_alcoholica;
    }

    public void setGraduacion_alcoholica(double graduacion_alcoholica) {
        this.graduacion_alcoholica = graduacion_alcoholica;
    }
    
    public double peso(){
        return volumen * 0.99 + volumen * 0.2;
    }
}

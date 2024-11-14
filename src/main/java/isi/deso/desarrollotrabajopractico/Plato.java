
package isi.deso.desarrollotrabajopractico;

public class Plato extends ItemMenu{
    private double peso;
    private int calorias;
    private boolean apto_vegano;
    private boolean apto_vegetariano;
    private boolean apto_celiaco;
    public Plato(){}
    public Plato(int id, String nombre, String descripcion, double precio, Categoria categoria, double peso, int calorias, boolean vegano, boolean vegetariano, boolean celiaco){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.peso = peso;
        this.calorias = calorias;
        apto_vegano = vegano;
        apto_vegetariano = vegetariano;
        apto_celiaco = celiaco;
    }
    
    public boolean esComida(){
        return true;
    }
    
    public boolean esBebida(){
        return false;
    }
    
    public boolean aptoVegano(){
        return apto_vegano;
    }
    
    public boolean aptoVegetariano(){
        return apto_vegetariano;
    }
    
    public boolean aptoCeliaco(){
        return apto_celiaco;
    }
    
    public int getCalorias(){
        return calorias;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setApto_vegano(boolean apto_vegano) {
        this.apto_vegano = apto_vegano;
    }

    public void setApto_vegetariano(boolean apto_vegetariano) {
        this.apto_vegetariano = apto_vegetariano;
    }

    public void setApto_celiaco(boolean apto_celiaco) {
        this.apto_celiaco = apto_celiaco;
    }
    
    public double peso(){
        peso = peso + peso * 0.1;
        return peso;
    }
    
}

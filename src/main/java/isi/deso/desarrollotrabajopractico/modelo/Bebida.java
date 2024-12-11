
package isi.deso.desarrollotrabajopractico.modelo;

public abstract class Bebida extends ItemMenu {
    
    protected double volumen;
   
    public boolean esBebida() {
        return true;
    }
    
    public boolean esComida() {
        return false;
    }
    
    public boolean aptoVegano() {
        return true;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }
    
}

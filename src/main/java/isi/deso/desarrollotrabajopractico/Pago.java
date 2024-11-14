
package isi.deso.desarrollotrabajopractico;

import java.time.LocalDate;

public abstract class Pago {
    protected LocalDate fecha;
    protected double monto;
    public abstract double calcularPrecio(double total);
    
    public void setMonto(double monto){
        this.monto = monto;
    }
}

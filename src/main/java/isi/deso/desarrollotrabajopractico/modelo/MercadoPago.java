
package isi.deso.desarrollotrabajopractico.modelo;

import java.time.LocalDate;

public class MercadoPago extends Pago {
    
    private String alias;

    public MercadoPago(String alias, LocalDate fecha) {
        this.alias = alias;
        this.fecha = fecha;
    }
    
    public double calcularPrecio(double total) {
       return total * 1.04; 
    }
}

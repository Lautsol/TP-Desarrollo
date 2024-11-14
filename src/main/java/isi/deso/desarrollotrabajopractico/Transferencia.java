
package isi.deso.desarrollotrabajopractico;

import java.time.LocalDate;


public class Transferencia extends Pago {
    private long cbu;
    private long cuit;
    
    public Transferencia(long cbu, long cuit, LocalDate fecha){
        this.cbu = cbu ;
        this.cuit = cuit; 
        this.fecha = fecha;
    }
  
    public double calcularPrecio(double total){
       return total * 1.02;
    }
    
}
